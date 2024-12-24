package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.registration.EmailDto;
import com.example.kptc_smp.exception.EmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final AssumptionService assumptionService;
    private final UserInformationService userInformationService;

    @Value("${spring.mail.username}")
    private String email;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Transactional
    public ResponseDto sendCode(EmailDto emailDto) {
        userInformationService.findByEmail(emailDto.getEmail()).ifPresent(u -> {throw new EmailException();});
        String key = generateVerificationCode();
        sendSimpleMessage(emailDto.getEmail(), "Код подтверждения", "Код: " + key);
        assumptionService.saveOrUpdate(emailDto.getEmail(), key);
        return new ResponseDto("Код отправлен");
    }

    public String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

}
