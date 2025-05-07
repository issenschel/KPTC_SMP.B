package com.example.kptc_smp.service.main.email;

import com.example.kptc_smp.dto.ActionTicketDto;
import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.email.CodeDto;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.entity.main.ActionTicket;
import com.example.kptc_smp.entity.main.EmailVerification;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.enums.EmailTemplateType;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.user.ActionTicketService;
import com.example.kptc_smp.service.main.user.UserInformationService;
import com.example.kptc_smp.service.main.user.UserService;
import com.example.kptc_smp.utility.email.EmailCodeGenerator;
import com.example.kptc_smp.utility.email.EmailMessageComposer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailVerificationService emailVerificationService;
    private final UserInformationService userInformationService;
    private final UserService userService;
    private final ActionTicketService actionTicketService;
    private final EmailSender emailSender;
    private final EmailCodeGenerator codeGenerator;
    private final EmailMessageComposer emailComposer;

    @Transactional
    public ResponseDto sendEmailCode(EmailDto emailDto) {
        userInformationService.findByEmail(emailDto.getEmail()).ifPresent(u -> { throw new EmailFoundException(); });

        sendVerificationEmail(emailDto.getEmail(), EmailTemplateType.REGISTRATION);

        return new ResponseDto("Код отправлен");
    }

    @Transactional
    public ResponseDto sendChangeEmailCode() {
        User user = userService.findWithUserInformationByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);

        sendVerificationEmail(user.getUserInformation().getEmail(), EmailTemplateType.EMAIL_CHANGE);

        return new ResponseDto("Код отправлен");
    }

    @Transactional
    public ActionTicketDto verifyCurrentEmailCode(CodeDto codeDto) {
        User user = userService.findWithUserInformationByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
        String email = user.getUserInformation().getEmail();
        EmailVerification verification = emailVerificationService.getValidatedEmailVerification(email, codeDto.getCode());

        ActionTicket actionTicket = actionTicketService.updateOrCreateActionTicket(user);

        emailVerificationService.delete(verification);

        return new ActionTicketDto(actionTicket.getTicket());
    }

    public ResponseDto sendPasswordResetLink(String email, String link) {
        String message = emailComposer.composePasswordResetEmail(link);
        emailSender.sendHtmlEmail(email, "Восстановление пароля", message);
        return new ResponseDto("Письмо отправлено");
    }

    private void sendVerificationEmail(String email, EmailTemplateType templateType) {
        String code = codeGenerator.generateVerificationCode();
        emailVerificationService.createOrUpdate(email, code);

        String message = emailComposer.composeVerificationEmail(code, templateType);
        emailSender.sendHtmlEmail(email, templateType.getHeader(), message);
    }

}