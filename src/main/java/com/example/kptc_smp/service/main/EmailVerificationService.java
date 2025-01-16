package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.EmailVerification;
import com.example.kptc_smp.repository.main.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public void createOrUpdate(String email, String code){
        Optional<EmailVerification> assumptionOptional = emailVerificationRepository.findByEmail(email);
        if (assumptionOptional.isPresent()) {
            changeEmailVerification(assumptionOptional.get(), code);
        } else {
            createEmailVerification(email, code);
        }
    }

    public void createEmailVerification(String email, String code) {
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(email);
        emailVerification.setCode(code);
        emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        emailVerificationRepository.save(emailVerification);
    }

    public void changeEmailVerification(EmailVerification emailVerification, String code) {
        emailVerification.setCode(code);
        emailVerificationRepository.save(emailVerification);
    }

    public boolean validateCode(EmailVerification emailVerification, String code) {
        return emailVerification.getCode().equals(code);
    }

    public boolean isExpired(EmailVerification emailVerification) {
        LocalDateTime now = LocalDateTime.now();
        return emailVerification.getExpiresAt().isBefore(now);
    }

    public void delete(EmailVerification emailVerification){
        emailVerificationRepository.delete(emailVerification);
    }

    public void deleteByEmail(String email) {
            emailVerificationRepository.deleteByEmail(email);
    }

    public Optional<EmailVerification> findByEmail(String email){
        return emailVerificationRepository.findByEmail(email);
    }
}
