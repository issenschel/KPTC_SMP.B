package com.example.kptc_smp.service.main.auth;

import com.example.kptc_smp.dto.auth.RegistrationUserDto;
import com.example.kptc_smp.entity.main.EmailVerification;
import com.example.kptc_smp.interfaces.ValidationRule;
import com.example.kptc_smp.service.main.email.EmailVerificationService;
import com.example.kptc_smp.service.main.user.UserInformationService;
import com.example.kptc_smp.service.main.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RegistrationValidatorService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final EmailVerificationService emailVerificationService;

    private final Map<String, ValidationRule> validationRules = new HashMap<>();

    @PostConstruct
    public void init() {
        validationRules.put("username", this::validateUsername);
        validationRules.put("passwordMatch", this::validatePasswordMatch);
        validationRules.put("email", this::validateEmail);
        validationRules.put("code", this::validateCode);
    }

    public Map<String, String> validateRegistration(RegistrationUserDto registrationUserDto) {
        Map<String, String> errors = new HashMap<>();

        validationRules.forEach((field, rule) ->
            rule.validate(registrationUserDto).ifPresent(errorMessage -> errors.put(field, errorMessage)));

        return errors;
    }

    public Optional<String> validateUsername(RegistrationUserDto registrationUserDto) {
        return userService.findByUsername(registrationUserDto.getUsername())
                .map(user -> "Логин уже занят");
    }

    public Optional<String> validatePasswordMatch(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return Optional.of("Пароли не совпадают");
        }
        return Optional.empty();
    }

    public Optional<String> validateCode(RegistrationUserDto registrationUserDto){
        Optional<EmailVerification> emailVerification = emailVerificationService.findByEmail(registrationUserDto.getEmail());
        if(emailVerification.isEmpty() || !emailVerificationService.validateCode(emailVerification.get(),registrationUserDto.getCode())){
            return Optional.of("Неверный код");
        }else if(emailVerificationService.isExpired(emailVerification.get())){
            return Optional.of("Время кода истекло");
        }
        return Optional.empty();
    }

    public Optional<String> validateEmail(RegistrationUserDto registrationUserDto) {
        return userInformationService.findByEmail(registrationUserDto.getEmail())
                .map(user -> "Почта уже занята");
    }
}
