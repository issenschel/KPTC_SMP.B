package com.example.kptc_smp.service;
import com.example.kptc_smp.dto.JwtRequest;
import com.example.kptc_smp.dto.JwtResponse;
import com.example.kptc_smp.dto.RegistrationUserDto;
import com.example.kptc_smp.dto.UserDetailsDto;
import com.example.kptc_smp.utils.EmailCode;
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
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailCode emailCode;
    private final AssumptionService assumptionService;
    private final RegistrationValidatorService registrationValidatorService;


    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        Map<String, String> validationsErrors = registrationValidatorService.validate(registrationUserDto);
        if (!validationsErrors.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(validationsErrors);
        }
        return registrationUserDto.getCode().isEmpty() ? registrationWithoutCode(registrationUserDto.getEmail())
                : registrationWithCode(registrationUserDto, registrationUserDto.getCode());
    }

    private ResponseEntity<?> registrationWithoutCode(String email){
        String key = emailCode.generateVerificationCode();
        emailService.sendSimpleMessage(email, "Код подтверждения", "Код: " + key);
        assumptionService.saveOrUpdate(email, key);
        return ResponseEntity.ok().body("Код отправлен");
    }

    private ResponseEntity<?> registrationWithCode(RegistrationUserDto registrationUserDto, String code){
        if (emailCode.validateCode(registrationUserDto.getEmail(), code)) {
            com.example.kptc_smp.entitys.UserDetails userDetails = userDetailsService.createNewUserDetails(registrationUserDto);
            assumptionService.delete(registrationUserDto.getEmail());
            return ResponseEntity.ok(new UserDetailsDto(userDetails.getId(), userDetails.getUser().getUsername(),
                    userDetails.getEmail(), userDetails.getMinecraftName()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный код");
    }

}
