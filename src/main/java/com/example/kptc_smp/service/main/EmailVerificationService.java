package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.EmailVerification;
import com.example.kptc_smp.repository.main.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationRepository emailVerificationRepository;

        @Transactional
        public void saveOrUpdate(String email, String code){
            Optional<EmailVerification> assumptionOptional = emailVerificationRepository.findByEmail(email);
            if (assumptionOptional.isPresent()) {
                EmailVerification emailVerification = assumptionOptional.get();
                emailVerification.setCode(code);
                emailVerificationRepository.save(emailVerification);
            } else {
                createNewAssumption(email, code);
            }
        }

        public void createNewAssumption(String email, String code) {
            EmailVerification emailVerification = new EmailVerification();
            emailVerification.setEmail(email);
            emailVerification.setCode(code);
            emailVerificationRepository.save(emailVerification);
        }

    public boolean validateCode(String email, String code) {
        return emailVerificationRepository.findByEmail(email).filter(emailVerification -> emailVerification.getCode().equals(code)).isPresent();
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
