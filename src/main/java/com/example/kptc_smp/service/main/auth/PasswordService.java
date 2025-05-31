package com.example.kptc_smp.service.main.auth;

import com.example.kptc_smp.exception.password.IncorrectCurrentPasswordException;
import com.example.kptc_smp.exception.password.PasswordMismatchException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    public void validateEncodedPasswordMatch(String currentPassword, String inputAtPassword) {
        if (!passwordEncoder.matches(currentPassword, inputAtPassword)) {
            throw new IncorrectCurrentPasswordException();
        }
    }

    public void validatePasswordEquals(String currentPassword, String inputAtPassword) {
        if (!currentPassword.equals(inputAtPassword)) {
            throw new PasswordMismatchException();
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
