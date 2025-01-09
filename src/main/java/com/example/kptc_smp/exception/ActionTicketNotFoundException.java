package com.example.kptc_smp.exception;

public class ActionTicketNotFoundException extends RuntimeException {
    public ActionTicketNotFoundException() {
        super("Тикет активности не найден");
    }
}
