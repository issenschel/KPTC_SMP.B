package com.example.kptc_smp.service.main.email;

import com.example.kptc_smp.enums.EmailTemplateType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailMessageComposerService {
    private final TemplateEngine templateEngine;

    @Value("${email.verification.template}")
    private String emailVerificationTemplate;

    @Value("${email.password.reset.template}")
    private String emailPasswordResetTemplate;

    public String composeVerificationEmail(String code, EmailTemplateType templateType) {
        Context context = new Context();
        context.setVariable("header", templateType.getHeader());
        context.setVariable("description", templateType.getDescription());
        context.setVariable("code", code);

        return templateEngine.process(emailVerificationTemplate, context);
    }

    public String composePasswordResetEmail(String link) {
        Context context = new Context();
        context.setVariable("link", link);

        return templateEngine.process(emailPasswordResetTemplate, context);
    }
}