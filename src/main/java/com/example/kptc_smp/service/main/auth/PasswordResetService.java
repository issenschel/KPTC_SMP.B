package com.example.kptc_smp.service.main.auth;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.PasswordResetDto;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.entity.main.ActionTicket;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserInformation;
import com.example.kptc_smp.enums.ActionType;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.email.EmailService;
import com.example.kptc_smp.service.main.user.*;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final ActionTicketService actionTicketService;
    private final UserInformationService userInformationService;
    private final EmailService emailService;
    private final PasswordService passwordService;
    private final AuthMeService authMeService;
    private final UserSessionService userSessionService;
    private final UserDataTokenService userDataTokenService;

    @Value("${password.reset.base.url}")
    private String passwordResetBaseUrl;

    @Transactional
    public ResponseDto createPasswordResetLink(EmailDto emailDto) {
        UserInformation userInformation = userInformationService.findWithUserByEmail(emailDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        User user = userInformation.getUser();
        ActionTicket actionTicket = actionTicketService.updateOrCreateActionTicket(user, ActionType.PASSWORD_RESET);
        String link = passwordResetBaseUrl + actionTicket.getTicket();

        return emailService.sendPasswordResetLink(emailDto.getEmail(), link);
    }

    @Transactional
    public ResponseDto resetPassword(String ticket, PasswordResetDto passwordResetDto) {
        ActionTicket actionTicket = actionTicketService.findByTicket(ticket).orElseThrow(ActionTicketNotFoundException::new);
        actionTicketService.validateActionTicket(actionTicket, ActionType.PASSWORD_RESET);

        passwordService.validatePasswordEquals(passwordResetDto.getPassword(), passwordResetDto.getConfirmPassword());

        changeUserPassword(actionTicket.getUser(), passwordResetDto);
        actionTicketService.delete(actionTicket);

        return new ResponseDto("Пароль успешно изменен");
    }

    private void changeUserPassword(User user, PasswordResetDto passwordResetDto) {
        String password = passwordService.encodePassword(passwordResetDto.getPassword());

        user.setPassword(password);
        authMeService.updatePassword(user.getUsername(), password);
        userDataTokenService.updateUserDataToken(user);

        userSessionService.deleteAllSessionsByUser(user);
    }
}
