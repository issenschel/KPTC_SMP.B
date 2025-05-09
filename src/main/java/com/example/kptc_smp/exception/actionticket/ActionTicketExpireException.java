package com.example.kptc_smp.exception.actionticket;

public class ActionTicketExpireException extends RuntimeException {
    public ActionTicketExpireException() {
        super("Тикет активности истёк");
    }
}
