package com.example.kptc_smp.service.main.email;

import com.example.kptc_smp.enums.EmailTemplateType;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageComposer {
    private static final String BASE_STYLE = """
                font-family: Arial, sans-serif;
                line-height: 1.6;
                max-width: 600px;
                margin: 0 auto;
                padding: 20px;
                border: 1px solid #e0e0e0;
            """;

    public String composeVerificationEmail(String code, EmailTemplateType templateType) {
        String header = templateType.getHeader();
        String description = templateType.getDescription();

        return """
                <html>
                    <body style="%s">
                        <div style="%s">
                            <h2 style="color: #2c3e50;">%s</h2>
                            <p>%s</p>
                            <div style="
                                background: #f8f9fa;
                                padding: 15px;
                                margin: 20px 0;
                                text-align: center;
                                font-size: 24px;
                                letter-spacing: 2px;
                                border-radius: 4px;">
                                <strong>%s</strong>
                            </div>
                            <p>Этот код действителен в течение 10 минут.</p>
                            <p style="color: #7f8c8d; font-size: 14px;">
                                Если вы не запрашивали этот код, пожалуйста, проигнорируйте это письмо.
                            </p>
                        </div>
                    </body>
                </html>
                """.formatted(
                BASE_STYLE,
                BASE_STYLE,
                header,
                description,
                code
        );
    }

    public String composePasswordResetEmail(String link) {
        return """
                <html>
                    <body style="%s">
                        <div style="%s; text-align: center;">
                            <h2 style="color: #2c3e50;">Восстановление пароля</h2>
                            <p>Мы получили запрос на сброс вашего пароля. Нажмите на кнопку ниже,
                            чтобы создать новый пароль.</p>
                            <div style="margin: 20px 0;">
                                <a href="%s" style="
                                    display: inline-block;
                                    background: #3498db;
                                    color: white;
                                    padding: 12px 24px;
                                    text-decoration: none;
                                    border-radius: 4px;
                                    font-weight: bold;">
                                    Создать новый пароль
                                </a>
                            </div>
                            <p>Если кнопка не работает, скопируйте и вставьте эту ссылку в браузер:</p>
                            <p style="word-break: break-all; background: #f8f9fa; padding: 10px; border-radius: 4px;">%s</p>
                            <p style="color: #7f8c8d; font-size: 14px;">
                                Если вы не запрашивали сброс пароля, пожалуйста, проигнорируйте это письмо.
                            </p>
                        </div>
                    </body>
                </html>
                """.formatted(BASE_STYLE, BASE_STYLE, link, link);
    }
}
