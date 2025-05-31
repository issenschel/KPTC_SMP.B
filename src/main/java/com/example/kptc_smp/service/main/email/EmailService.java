package com.example.kptc_smp.service.main.email;

import com.example.kptc_smp.dto.ActionTicketResponseDto;
import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.email.CodeRequestDto;
import com.example.kptc_smp.dto.email.EmailRequestDto;
import com.example.kptc_smp.model.main.ActionTicket;
import com.example.kptc_smp.model.main.EmailVerification;
import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.enums.ActionType;
import com.example.kptc_smp.enums.EmailTemplateType;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.user.ActionTicketService;
import com.example.kptc_smp.service.main.user.UserInformationService;
import com.example.kptc_smp.service.main.user.UserService;
import com.example.kptc_smp.utility.email.EmailCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final EmailSenderService emailSenderService;
    private final EmailCodeGenerator codeGenerator;
    private final EmailMessageComposerService emailComposer;

    @Value("${message.code.sent}")
    private String codeSentMessage;

    @Value("${message.email.sent}")
    private String emailSentMessage;

    @Value("${message.email.password.reset.subject}")
    private String passwordResetSubject;

    @Transactional
    public ResponseDto sendEmailCode(EmailRequestDto emailRequestDto) {
        userInformationService.findByEmail(emailRequestDto.getEmail()).ifPresent(u -> { throw new EmailFoundException(); });

        sendVerificationEmail(emailRequestDto.getEmail(), EmailTemplateType.REGISTRATION);

        return new ResponseDto(codeSentMessage);
    }

    @Transactional
    public ResponseDto sendChangeEmailCode() {
        User user = userService.findWithUserInformationByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);

        sendVerificationEmail(user.getUserInformation().getEmail(), EmailTemplateType.EMAIL_CHANGE);

        return new ResponseDto(codeSentMessage);
    }

    @Transactional
    public ActionTicketResponseDto verifyCurrentEmailCode(CodeRequestDto codeRequestDto) {
        User user = userService.findWithUserInformationByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
        String email = user.getUserInformation().getEmail();
        EmailVerification verification = emailVerificationService.findValidEmailVerification(email, codeRequestDto.getCode());

        ActionTicket actionTicket = actionTicketService.updateOrCreateActionTicket(user, ActionType.EMAIL_CHANGE);

        emailVerificationService.delete(verification);

        return new ActionTicketResponseDto(actionTicket.getTicket());
    }

    public ResponseDto sendPasswordResetLink(String email, String link) {
        String message = emailComposer.composePasswordResetEmail(link);
        emailSenderService.sendHtmlEmail(email, passwordResetSubject, message);
        return new ResponseDto(emailSentMessage);
    }

    private void sendVerificationEmail(String email, EmailTemplateType templateType) {
        String code = codeGenerator.generateVerificationCode();
        emailVerificationService.createOrUpdate(email, code);

        String message = emailComposer.composeVerificationEmail(code, templateType);
        emailSenderService.sendHtmlEmail(email, templateType.getHeader(), message);
    }

}