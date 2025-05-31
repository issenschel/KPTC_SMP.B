package com.example.kptc_smp.service.main.auth;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.PasswordResetRequestDto;
import com.example.kptc_smp.dto.email.EmailRequestDto;
import com.example.kptc_smp.model.main.ActionTicket;
import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.model.main.UserInformation;
import com.example.kptc_smp.enums.ActionType;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.email.EmailService;
import com.example.kptc_smp.service.main.user.*;
import com.example.kptc_smp.service.main.validator.ActionTickerValidatorService;
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
    private final ActionTickerValidatorService actionTickerValidatorService;

    @Value("${password.reset.base.url}")
    private String passwordResetBaseUrl;

    @Value("${message.password.changed}")
    private String passwordChangedMessage;

    @Transactional
    public ResponseDto createPasswordResetLink(EmailRequestDto emailRequestDto) {
        UserInformation userInformation = userInformationService.findWithUserByEmail(emailRequestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        User user = userInformation.getUser();
        ActionTicket actionTicket = actionTicketService.updateOrCreateActionTicket(user, ActionType.PASSWORD_RESET);
        String link = passwordResetBaseUrl + actionTicket.getTicket();

        return emailService.sendPasswordResetLink(emailRequestDto.getEmail(), link);
    }

    @Transactional
    public ResponseDto resetPassword(String ticket, PasswordResetRequestDto passwordResetRequestDto) {
        ActionTicket actionTicket = actionTicketService.findByTicket(ticket).orElseThrow(ActionTicketNotFoundException::new);
        actionTickerValidatorService.validateActionTicket(actionTicket, ActionType.PASSWORD_RESET);

        passwordService.validatePasswordEquals(passwordResetRequestDto.getPassword(), passwordResetRequestDto.getConfirmPassword());

        changeUserPassword(actionTicket.getUser(), passwordResetRequestDto);
        actionTicketService.delete(actionTicket);

        return new ResponseDto(passwordChangedMessage);
    }

    private void changeUserPassword(User user, PasswordResetRequestDto passwordResetRequestDto) {
        String password = passwordService.encodePassword(passwordResetRequestDto.getPassword());

        user.setPassword(password);
        authMeService.updatePassword(user.getUsername(), password);
        userDataTokenService.updateUserDataToken(user);

        userSessionService.deleteAllSessionsByUser(user);
    }
}
