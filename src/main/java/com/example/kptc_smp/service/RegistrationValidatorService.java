package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.RegistrationUserDto;
import com.example.kptc_smp.interfaces.ValidationRule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RegistrationValidatorService {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    private final Map<String, ValidationRule> validationRules = new HashMap<>();

    @PostConstruct
    public void init() {
        validationRules.put("email", this::validateEmail);
        validationRules.put("username", this::validateUsername);
        validationRules.put("minecraftName", this::validateMinecraftName);
        validationRules.put("passwordMatch", this::validatePasswordMatch);
    }

    public Map<String, String> validate(RegistrationUserDto registrationUserDto) {
        Map<String, String> errors = new HashMap<>();

        validationRules.forEach((field, rule) ->
            rule.validate(registrationUserDto).ifPresent(errorMessage -> errors.put(field, errorMessage)));

        return errors;
    }

    private Optional<String> validateEmail(RegistrationUserDto registrationUserDto) {
        return userDetailsService.findByEmail(registrationUserDto.getEmail())
                .map(user -> "Почта уже занята");
    }

    private Optional<String> validateUsername(RegistrationUserDto registrationUserDto) {
        return userService.findByUsername(registrationUserDto.getUsername())
                .map(user -> "Логин уже занят");
    }

    private Optional<String> validateMinecraftName(RegistrationUserDto registrationUserDto) {
        return userDetailsService.findByMinecraftName(registrationUserDto.getMinecraftName())
                .map(user -> "Никнейм занят");
    }

    private Optional<String> validatePasswordMatch(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return Optional.of("Пароли не совпадают");
        }
        return Optional.empty();
    }
}
