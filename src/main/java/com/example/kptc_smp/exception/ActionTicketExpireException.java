package com.example.kptc_smp.exception;

public class ActionTicketExpireException extends RuntimeException {
    public ActionTicketExpireException() {
        super("Тикет активности истёк");
    }
}
