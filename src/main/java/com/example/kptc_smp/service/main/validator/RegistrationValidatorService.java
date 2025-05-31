package com.example.kptc_smp.service.main.validator;

import com.example.kptc_smp.dto.auth.RegistrationUserRequestDto;
import com.example.kptc_smp.model.main.EmailVerification;
import com.example.kptc_smp.interfaces.ValidationRule;
import com.example.kptc_smp.service.main.email.EmailVerificationService;
import com.example.kptc_smp.service.main.user.UserInformationService;
import com.example.kptc_smp.service.main.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RegistrationValidatorService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final EmailVerificationService emailVerificationService;
    private final CodeValidatorService codeValidatorService;

    private final Map<String, ValidationRule> validationRules = new HashMap<>();

    @Value("${message.username.taken}")
    private String usernameTakenMessage;

    @Value("${message.password.mismatch}")
    private String passwordsNotMatchMessage;

    @Value("${message.email.taken}")
    private String emailTakenMessage;

    @Value("${message.code.invalid}")
    private String invalidCodeMessage;

    @Value("${message.code.expired}")
    private String codeExpiredMessage;

    @PostConstruct
    public void init() {
        validationRules.put("username", this::validateUsername);
        validationRules.put("passwordMatch", this::validatePasswordMatch);
        validationRules.put("email", this::validateEmail);
        validationRules.put("code", this::validateCode);
    }

    public Map<String, String> validateRegistration(RegistrationUserRequestDto registrationUserRequestDto) {
        Map<String, String> errors = new HashMap<>();

        validationRules.forEach((field, rule) ->
            rule.validate(registrationUserRequestDto).ifPresent(errorMessage -> errors.put(field, errorMessage)));

        return errors;
    }

    public Optional<String> validateUsername(RegistrationUserRequestDto registrationUserRequestDto) {
        return userService.findByUsername(registrationUserRequestDto.getUsername())
                .map(user -> usernameTakenMessage);
    }

    public Optional<String> validatePasswordMatch(RegistrationUserRequestDto registrationUserRequestDto) {
        if (!registrationUserRequestDto.getPassword().equals(registrationUserRequestDto.getConfirmPassword())) {
            return Optional.of(passwordsNotMatchMessage);
        }
        return Optional.empty();
    }

    public Optional<String> validateCode(RegistrationUserRequestDto registrationUserRequestDto){
        Optional<EmailVerification> emailVerification = emailVerificationService.findByEmail(registrationUserRequestDto.getEmail());

        if(emailVerification.isEmpty() || !codeValidatorService.isCodeValid(emailVerification.get(), registrationUserRequestDto.getCode())){
            return Optional.of(invalidCodeMessage);
        }else if(codeValidatorService.isVerificationExpired(emailVerification.get())){
            return Optional.of(codeExpiredMessage);
        }

        return Optional.empty();
    }

    public Optional<String> validateEmail(RegistrationUserRequestDto registrationUserRequestDto) {
        return userInformationService.findByEmail(registrationUserRequestDto.getEmail())
                .map(user -> emailTakenMessage);
    }
}
