package com.example.kptc_smp.exception.actionticket;

public class ActionTicketNotFoundException extends RuntimeException {
    public ActionTicketNotFoundException() {
        super("Тикет активности не найден");
    }
}
