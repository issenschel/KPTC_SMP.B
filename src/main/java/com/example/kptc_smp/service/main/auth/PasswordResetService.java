package com.example.kptc_smp.service.main.auth;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.PasswordResetDto;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.entity.main.PasswordReset;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserInformation;
import com.example.kptc_smp.exception.auth.PasswordResetDateExpiredException;
import com.example.kptc_smp.exception.auth.PasswordResetUUIDNotFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.repository.main.PasswordResetRepository;
import com.example.kptc_smp.service.main.email.EmailService;
import com.example.kptc_smp.service.main.user.UserInformationService;
import com.example.kptc_smp.service.main.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final EmailService emailService;
    private final PasswordService passwordService;

    @Value("${password.reset.base.url}")
    private String passwordResetBaseUrl;

    @Transactional
    public ResponseDto createPasswordResetLink(EmailDto emailDto) {
        UserInformation userInformation = userInformationService.findWithUserByEmail(emailDto.getEmail()).orElseThrow(UserNotFoundException::new);
        UUID linkUUID = UUID.randomUUID();
        createOrUpdatePasswordReset(userInformation.getUser(), linkUUID);
        String link = passwordResetBaseUrl + linkUUID;
        return emailService.sendPasswordResetLink(emailDto.getEmail(), link);
    }

    @Transactional
    public void createOrUpdatePasswordReset(User user, UUID linkUUID) {
        passwordResetRepository.findByUser(user).ifPresentOrElse(passwordReset -> updatePasswordReset(passwordReset,linkUUID),
                ()-> createPasswordReset(user,linkUUID));
    }

    public void updatePasswordReset(PasswordReset passwordReset, UUID linkUUID) {
        passwordReset.setLinkUUID(linkUUID);
        passwordReset.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        passwordResetRepository.save(passwordReset);
    }

    public void createPasswordReset(User user, UUID linkUUID) {
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setLinkUUID(linkUUID);
        passwordReset.setUser(user);
        passwordReset.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        passwordResetRepository.save(passwordReset);
    }

    @Transactional
    public ResponseDto resetPassword(UUID linkUUID, PasswordResetDto passwordResetDto) {
        PasswordReset passwordReset = passwordResetRepository.findByLinkUUID(linkUUID).orElseThrow(PasswordResetUUIDNotFoundException::new);
        isDateExpired(passwordReset);
        passwordService.validatePasswordEquals(passwordResetDto.getPassword(), passwordResetDto.getConfirmPassword());
        changeUserPassword(passwordReset, passwordResetDto);
        passwordResetRepository.delete(passwordReset);
        return new ResponseDto("Пароль изменен");
    }

    private void isDateExpired(PasswordReset passwordReset) {
        LocalDateTime now = LocalDateTime.now();
        if (passwordReset.getExpiresAt().isBefore(now)) {
            throw new PasswordResetDateExpiredException();
        }
    }

    private void changeUserPassword(PasswordReset passwordReset, PasswordResetDto passwordResetDto) {
        User user = passwordReset.getUser();
        user.setPassword(passwordService.encodePassword(passwordResetDto.getPassword()));
        userService.saveUser(user);
    }
}
