package com.example.kptc_smp.service.main.validator;

import com.example.kptc_smp.model.main.EmailVerification;
import com.example.kptc_smp.exception.email.CodeExpireException;
import com.example.kptc_smp.exception.email.CodeValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CodeValidatorService {

    public void validateCode(EmailVerification emailVerification, String code) {
        if (!isCodeValid(emailVerification, code)) {
            throw new CodeValidationException();
        } else if (isVerificationExpired(emailVerification)) {
            throw new CodeExpireException();
        }
    }

    public boolean isCodeValid(EmailVerification emailVerification, String code) {
        return emailVerification.getCode().equals(code);
    }

    public boolean isVerificationExpired(EmailVerification emailVerification) {
        LocalDateTime now = LocalDateTime.now();
        return emailVerification.getExpiresAt().isBefore(now);
    }

}
