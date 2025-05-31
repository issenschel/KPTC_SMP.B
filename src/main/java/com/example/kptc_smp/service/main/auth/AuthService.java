package com.example.kptc_smp.service.main.auth;

import com.example.kptc_smp.dto.auth.JwtTokenPairResponseDto;
import com.example.kptc_smp.dto.auth.AuthResponseDto;
import com.example.kptc_smp.dto.auth.AuthRequestDto;
import com.example.kptc_smp.dto.auth.RegistrationUserRequestDto;
import com.example.kptc_smp.dto.profile.UserAccountDetailsResponseDto;
import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.model.main.UserInformation;
import com.example.kptc_smp.model.main.UserSession;
import com.example.kptc_smp.exception.auth.RegistrationValidationException;
import com.example.kptc_smp.exception.jwt.JwtNotFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.email.EmailVerificationService;
import com.example.kptc_smp.service.main.user.UserDataTokenService;
import com.example.kptc_smp.service.main.user.UserInformationService;
import com.example.kptc_smp.service.main.user.UserService;
import com.example.kptc_smp.service.main.user.UserSessionService;
import com.example.kptc_smp.service.main.validator.JwtTokenValidatorService;
import com.example.kptc_smp.service.main.validator.RegistrationValidatorService;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import com.example.kptc_smp.service.minecraft.WhitelistService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDataTokenService userDataTokenService;
    private final EmailVerificationService emailVerificationService;
    private final RegistrationValidatorService registrationValidatorService;
    private final WhitelistService whitelistService;
    private final AuthMeService authMeService;
    private final UserSessionService userSessionService;
    private final JwtTokenValidatorService jwtTokenValidatorService;

    @Transactional
    public AuthResponseDto authenticate(@RequestBody AuthRequestDto authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        User user = userService.findWithUserDataTokenByUsername(authRequest.getUsername()).orElseThrow(UserNotFoundException::new);

        UUID tokenUUID = user.getUserDataToken().getTokenUUID();
        UserSession userSession = userSessionService.createSession(user);
        String accessToken = jwtTokenUtils.generateAccessToken(user.getUsername(),tokenUUID);
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return new AuthResponseDto(new JwtTokenPairResponseDto(userSession.getRefreshToken(), accessToken), roles);
    }

    @Transactional
    public JwtTokenPairResponseDto refreshToken(String refreshToken) {
        jwtTokenValidatorService.validateToken(refreshToken);

        String username = jwtTokenUtils.getUsername(refreshToken);
        User user = userService.findWithSessionsAndTokenByUsername(username).orElseThrow(UserNotFoundException::new);

        UserSession userSession = user.getUserSessions().stream().filter(t -> t.getRefreshToken().equals(refreshToken))
                .findFirst().orElseThrow(JwtNotFoundException::new);

        String newAccessToken = jwtTokenUtils.generateAccessToken(username,user.getUserDataToken().getTokenUUID());
        String newRefreshToken = jwtTokenUtils.generateRefreshToken(username);

        userSession.setRefreshToken(newRefreshToken);

        return new JwtTokenPairResponseDto(newRefreshToken, newAccessToken);
    }

    @Transactional(transactionManager = "chainedTransactionManager")
    public UserAccountDetailsResponseDto registrationUser(@RequestBody RegistrationUserRequestDto registrationUserRequestDto) {
        validateRegistration(registrationUserRequestDto);

        User user = userService.createUser(registrationUserRequestDto.getUsername(), registrationUserRequestDto.getPassword());
        UserInformation userInformation = userInformationService.createNewUserInformation(registrationUserRequestDto, user);
        createUserDataToken(user);
        emailVerificationService.deleteByEmail(registrationUserRequestDto.getEmail());
        registrationMinecraftUser(user);

        return new UserAccountDetailsResponseDto(userInformation.getId(), userInformation.getUser().getUsername(),
                userInformation.getEmail(), userInformation.getRegistrationDate());
    }

    private void validateRegistration(RegistrationUserRequestDto registrationUserRequestDto) {
        Map<String, String> validationsErrors = registrationValidatorService.validateRegistration(registrationUserRequestDto);

        if (!validationsErrors.isEmpty()) {
            throw new RegistrationValidationException(validationsErrors);
        }
    }

    private void registrationMinecraftUser(User user) {
        authMeService.createAuthMe(user);
        whitelistService.createWhitelist(user.getUsername());
    }

    private void createUserDataToken(User user) {
        UUID tokenUUID = UUID.randomUUID();
        userDataTokenService.createUserDataToken(user, tokenUUID);
    }

}
