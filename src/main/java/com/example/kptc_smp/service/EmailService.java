package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.registration.EmailDto;
import com.example.kptc_smp.exception.ChangeEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final AssumptionService assumptionService;
    private final UserInformationService userInformationService;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kptcsmp@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Transactional
    public String sendCode(EmailDto emailDto) {
        userInformationService.findByEmail(emailDto.getEmail()).ifPresent(u -> {throw new ChangeEmailException();});
        String key = generateVerificationCode();
        sendSimpleMessage(emailDto.getEmail(), "Код подтверждения", "Код: " + key);
        assumptionService.saveOrUpdate(emailDto.getEmail(), key);
        return "Код отправлен";
    }

    public String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

}
