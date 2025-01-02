package com.example.kptc_smp.interfaces;

import com.example.kptc_smp.dto.registration.RegistrationUserDto;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRule {
    Optional<String> validate(RegistrationUserDto registrationUserDto);
}
