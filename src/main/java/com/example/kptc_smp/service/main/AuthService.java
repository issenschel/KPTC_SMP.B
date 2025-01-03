package com.example.kptc_smp.service.main;
import com.example.kptc_smp.dto.auth.AuthTokenDto;
import com.example.kptc_smp.dto.auth.JwtRequestDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.dto.registration.RegistrationUserDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserInformation;
import com.example.kptc_smp.exception.registration.RegistrationValidationException;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;
    private final EmailVerificationService emailVerificationService;
    private final RegistrationValidatorService registrationValidatorService;
    private final WhitelistService whitelistService;
    private final AuthMeService authMeService;

    @Transactional
    public AuthTokenDto createAuthToken(@RequestBody JwtRequestDto authRequest) throws BadCredentialsException {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            Optional<User> user = userService.findWithTokenVersionByUsername(authRequest.getUsername());
            UUID tokenUUID = user.orElseThrow(() -> new BadCredentialsException("")).getAuthToken().getTokenUUID();
            String token = jwtTokenUtils.generateToken(authentication, tokenUUID);
            List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            return new AuthTokenDto(token,roles);
    }

    @Transactional
    public UserInformationDto registrationUser(@RequestBody RegistrationUserDto registrationUserDto) {
            validateRegistration(registrationUserDto);
            User user = userService.createNewUser(registrationUserDto.getUsername(), registrationUserDto.getPassword());
            UserInformation userInformation = userInformationService.createNewUserInformation(registrationUserDto, user);
            minecraftRegistrationActions(user);
            UUID tokenUUID = UUID.randomUUID();
            authTokenService.createAuthToken(user,tokenUUID);
            emailVerificationService.deleteByEmail(registrationUserDto.getEmail());
            return new UserInformationDto(userInformation.getId(), userInformation.getUser().getUsername(),
                    userInformation.getEmail());
    }

    private void validateRegistration(RegistrationUserDto registrationUserDto) {
        Map<String, String> validationsErrors = registrationValidatorService.validate(registrationUserDto);
        if (!validationsErrors.isEmpty()) {
            throw new RegistrationValidationException(validationsErrors);
        }
    }

    private void minecraftRegistrationActions(User user) {
        authMeService.createAuthMe(user);
        whitelistService.createWhitelist(user.getUsername());
    }
    
}
