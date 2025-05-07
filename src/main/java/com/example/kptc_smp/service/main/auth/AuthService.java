package com.example.kptc_smp.service.main.auth;

import com.example.kptc_smp.dto.JwtTokenPairDto;
import com.example.kptc_smp.dto.auth.AuthResponseDto;
import com.example.kptc_smp.dto.auth.JwtRequestDto;
import com.example.kptc_smp.dto.auth.RegistrationUserDto;
import com.example.kptc_smp.dto.profile.UserAccountDetailsDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserInformation;
import com.example.kptc_smp.entity.main.UserSession;
import com.example.kptc_smp.exception.auth.RegistrationValidationException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.email.EmailVerificationService;
import com.example.kptc_smp.service.main.user.UserDataTokenService;
import com.example.kptc_smp.service.main.user.UserInformationService;
import com.example.kptc_smp.service.main.user.UserService;
import com.example.kptc_smp.service.main.user.UserSessionService;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import com.example.kptc_smp.service.minecraft.WhitelistService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Transactional
    public AuthResponseDto authenticate(@RequestBody JwtRequestDto authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        User user = userService.findWithTokenVersionByUsername(authRequest.getUsername()).orElseThrow(() -> new BadCredentialsException(""));

        UUID tokenUUID = user.getUserDataToken().getTokenUUID();
        UserSession userSession = userSessionService.createSession(user);
        String accessToken = jwtTokenUtils.generateAccessToken(tokenUUID);
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return new AuthResponseDto(new JwtTokenPairDto(userSession.getRefreshToken(), accessToken), roles);
    }

    @Transactional
    public JwtTokenPairDto refreshToken(String refreshToken) {
        if (jwtTokenUtils.isTokenExpired(refreshToken)) {
            throw new BadCredentialsException("Refresh token expired");
        }

        String username = jwtTokenUtils.getUsername(refreshToken);
        User user = userService.findWithSessionsAndTokenByUsername(username).orElseThrow(() -> new BadCredentialsException("User not found"));

        UserSession userSession = user.getUserSessions().stream().filter(t -> t.getRefreshToken().equals(refreshToken))
                .findFirst().orElseThrow(UserNotFoundException::new);

        String newAccessToken = jwtTokenUtils.generateAccessToken(user.getUserDataToken().getTokenUUID());
        String newRefreshToken = jwtTokenUtils.generateRefreshToken();

        userSession.setRefreshToken(newRefreshToken);

        return new JwtTokenPairDto(newRefreshToken, newAccessToken);
    }

    @Transactional(transactionManager = "chainedTransactionManager")
    public UserAccountDetailsDto registrationUser(@RequestBody RegistrationUserDto registrationUserDto) {
        validateRegistration(registrationUserDto);

        User user = userService.createUser(registrationUserDto.getUsername(), registrationUserDto.getPassword());
        UserInformation userInformation = userInformationService.createNewUserInformation(registrationUserDto, user);
        createUserDataToken(user);
        emailVerificationService.deleteByEmail(registrationUserDto.getEmail());
        registrationMinecraftUser(user);

        return new UserAccountDetailsDto(userInformation.getId(), userInformation.getUser().getUsername(),
                userInformation.getEmail(), userInformation.getRegistrationDate());
    }

    private void validateRegistration(RegistrationUserDto registrationUserDto) {
        Map<String, String> validationsErrors = registrationValidatorService.validateRegistration(registrationUserDto);

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
