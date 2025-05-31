package com.example.kptc_smp.service.main.email;

import com.example.kptc_smp.model.main.EmailVerification;
import com.example.kptc_smp.exception.email.EmailVerificationNotFoundException;
import com.example.kptc_smp.repository.main.EmailVerificationRepository;
import com.example.kptc_smp.service.main.validator.CodeValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final CodeValidatorService codeValidatorService;

    @Value("${email.verification.expiration.time}")
    private int emailVerificationExpirationTime;

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
        emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(emailVerificationExpirationTime));
        emailVerificationRepository.save(emailVerification);
    }

    public void changeEmailVerification(EmailVerification emailVerification, String code) {
        emailVerification.setCode(code);
        emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(emailVerificationExpirationTime));
        emailVerificationRepository.save(emailVerification);
    }

    public EmailVerification findValidEmailVerification(String email, String code) {
        EmailVerification emailVerification = findByEmail(email).orElseThrow(EmailVerificationNotFoundException::new);

        codeValidatorService.validateCode(emailVerification, code);

        return emailVerification;
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
