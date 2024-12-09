package com.example.kptc_smp.service;
import com.example.kptc_smp.dto.auth.AuthTokenDto;
import com.example.kptc_smp.dto.auth.JwtRequestDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.dto.registration.RegistrationUserDto;
import com.example.kptc_smp.entity.User;
import com.example.kptc_smp.entity.UserInformation;
import com.example.kptc_smp.exception.RegistrationValidationException;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final TokenVersionService tokenVersionService;
    private final AssumptionService assumptionService;
    private final RegistrationValidatorService registrationValidatorService;


    public AuthTokenDto createAuthToken(@RequestBody JwtRequestDto authRequest) throws BadCredentialsException {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
            Optional<User> user = userService.findByUsername(authRequest.getUsername());
            String versionId = user.orElseThrow(() -> new BadCredentialsException("Нет версии токена")).getTokenVersion().getVersion();
            String token = jwtTokenUtils.generateToken(userDetails, versionId);
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            return new AuthTokenDto(token,roles);
    }

    @Transactional
    public UserInformationDto createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
            Map<String, String> validationsErrors = registrationValidatorService.validate(registrationUserDto);
            if (!validationsErrors.isEmpty()){
                throw new RegistrationValidationException(validationsErrors);
            }
            User user = userService.createNewUser(registrationUserDto.getUsername(), registrationUserDto.getPassword());
            UserInformation userInformation = userInformationService.createNewUserInformation(registrationUserDto, user);
            String token = UUID.randomUUID().toString();
            tokenVersionService.createNewTokenVersion(user,token);
            assumptionService.findByEmail(registrationUserDto.getEmail()).ifPresent(assumptionService::delete);
            return new UserInformationDto(userInformation.getId(), userInformation.getUser().getUsername(),
                    userInformation.getEmail(), userInformation.getMinecraftName());
    }

}
