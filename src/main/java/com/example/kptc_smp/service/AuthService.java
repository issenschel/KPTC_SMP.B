package com.example.kptc_smp.service;
import com.example.kptc_smp.dto.JwtRequestDto;
import com.example.kptc_smp.dto.JwtResponseDto;
import com.example.kptc_smp.dto.RegistrationUserDto;
import com.example.kptc_smp.dto.UserInformationDto;
import com.example.kptc_smp.entitys.UserInformation;
import com.example.kptc_smp.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final EmailService emailService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final AssumptionService assumptionService;
    private final RegistrationValidatorService registrationValidatorService;


    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDto authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        Map<String, String> validationsErrors = registrationValidatorService.validate(registrationUserDto);
        if (!validationsErrors.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(validationsErrors);
        }
        return registrationUserDto.getCode().isEmpty() ? emailService.sendCode(registrationUserDto.getEmail())
                : registrationWithCode(registrationUserDto, registrationUserDto.getCode());
    }

    private ResponseEntity<?> registrationWithCode(RegistrationUserDto registrationUserDto, String code){
        if (emailService.validateCode(registrationUserDto.getEmail(), code)) {
            UserInformation userInformation = userInformationService.createNewUserDetails(registrationUserDto);
            assumptionService.findByEmail(registrationUserDto.getEmail()).ifPresent(assumptionService::delete);
            return ResponseEntity.ok(new UserInformationDto(userInformation.getId(), userInformation.getUser().getUsername(),
                    userInformation.getEmail(), userInformation.getMinecraftName()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный код");
    }

}
