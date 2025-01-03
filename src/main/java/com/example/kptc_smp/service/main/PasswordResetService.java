package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.PasswordResetDto;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.entity.main.PasswordReset;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserInformation;
import com.example.kptc_smp.exception.auth.PasswordResetDateExpiredException;
import com.example.kptc_smp.exception.auth.TokenNotFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.repository.main.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final EmailService emailService;
    private final PasswordService passwordService;

    public ResponseDto createPasswordResetLink(EmailDto emailDto) {
        UserInformation userInformation = userInformationService.findWithUserByEmail(emailDto.getEmail()).orElseThrow(UserNotFoundException::new);
        UUID linkUUID = UUID.randomUUID();
        createPasswordReset(userInformation.getUser(), linkUUID);
        emailService.sendSimpleMessage(emailDto.getEmail(), "Ссылка для смены пароля", "ssilka" + linkUUID);
        return new ResponseDto("Письмо отправлено");
    }

    public void createPasswordReset(User user, UUID linkUUID) {
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setLinkUUID(linkUUID);
        passwordReset.setUser(user);
        passwordResetRepository.save(passwordReset);
    }

    public void changeUserPassword(UUID linkUUID, PasswordResetDto passwordResetDto) {
        PasswordReset passwordReset = passwordResetRepository.findByLinkUUID(linkUUID).orElseThrow(TokenNotFoundException::new);
        if (isDateExpired(passwordReset)) {
            throw new PasswordResetDateExpiredException();
        }
        passwordService.validatePasswordMatch(passwordResetDto.getPassword(), passwordResetDto.getConfirmPassword());
        User user = passwordReset.getUser();
        user.setPassword(passwordService.encodePassword(passwordResetDto.getPassword()));
        userService.saveUser(user);
    }

    private boolean isDateExpired(PasswordReset passToken) {
        Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
