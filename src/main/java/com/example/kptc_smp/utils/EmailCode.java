package com.example.kptc_smp.utils;

import com.example.kptc_smp.repositories.AssumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmailCode {
    private final AssumptionRepository assumptionRepository;


    public String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public boolean validateCode(String email, String code) {
        return assumptionRepository.findByEmail(email).filter(assumption -> assumption.getCode().equals(code)).isPresent();
    }
}
