package com.example.kptc_smp.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateType {
    REGISTRATION(
            "Подтверждение email",
            "Для завершения процесса подтвердите ваш email."
    ),
    EMAIL_CHANGE(
            "Подтверждение смены email",
            "Для завершения процесса смены email, пожалуйста, подтвердите новый адрес."
    );

    private final String header;
    private final String description;

    EmailTemplateType(String header, String description) {
        this.header = header;
        this.description = description;
    }

}
