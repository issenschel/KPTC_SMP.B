package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailVerificationService emailVerificationService;
    private final UserInformationService userInformationService;
    private final UserService userService;

    @Value("${spring.mail.username}")
    private String email;

    @Transactional
    public ResponseDto sendRegistrationCode(EmailDto emailDto) {
        userInformationService.findByEmail(emailDto.getEmail()).ifPresent(u -> {
            throw new EmailFoundException();
        });
        sendCode(emailDto.getEmail());
        return new ResponseDto("Код отправлен");
    }

    @Transactional
    public ResponseDto sendChangeEmailCode() {
        User user = userService.findWithUserInformationAndTokenVersionByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
        sendCode(user.getUserInformation().getEmail());
        return new ResponseDto("Код отправлен");
    }


    public void sendCode(String email) {
        String key = generateVerificationCode();
        emailVerificationService.createOrUpdate(email, key);
        String subject = "Код подтверждения";
        String message = "Код: " + key;
        sendSimpleMessage(email, subject, message);
    }

    public ResponseDto sendPasswordResetLink(String email, String link) {
        String subject = "Ссылка для смены пароля";
        String message = "Ссылка: " + link;
        sendSimpleMessage(email, subject, message);
        return new ResponseDto("Письмо отправлено");
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

}
