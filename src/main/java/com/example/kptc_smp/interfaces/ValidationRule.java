package com.example.kptc_smp.interfaces;

import com.example.kptc_smp.dto.auth.RegistrationUserRequestDto;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRule {
    Optional<String> validate(RegistrationUserRequestDto registrationUserRequestDto);
}
