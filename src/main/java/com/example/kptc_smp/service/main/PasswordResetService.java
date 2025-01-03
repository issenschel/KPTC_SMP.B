package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.auth.PasswordChangeDto;
import com.example.kptc_smp.entity.main.PasswordReset;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.auth.TokenNotFoundException;
import com.example.kptc_smp.exception.profile.PasswordValidationException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.repository.main.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(String username){
        User user = userService.findWithUserInformationByUsername(username).orElseThrow(UserNotFoundException::new);
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        emailService.sendSimpleMessage(user.getUserInformation().getEmail(),"Ссылка для смены пароля","http://localhost:5174/change-password?token="+token);
    }

    public void createPasswordResetTokenForUser(User user,String token) {
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setToken(token);
        passwordReset.setUser(user);
        passwordResetRepository.save(passwordReset);
    }

    public void changeUserPassword(String token, PasswordChangeDto passwordChangeDto) {
        PasswordReset passToken = passwordResetRepository.findByToken(token).orElseThrow(TokenNotFoundException::new);
        if(isTokenExpired(passToken)){

        }
        if (!passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword())) {
            throw new PasswordValidationException("Новый пароль и подтверждение пароля не совпадают");
        }
        User user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));
        userService.saveUser(user);
    }

    private boolean isTokenExpired(PasswordReset passToken) {
        Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
