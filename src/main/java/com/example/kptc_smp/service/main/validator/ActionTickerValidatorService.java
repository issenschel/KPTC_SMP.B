package com.example.kptc_smp.service.main.validator;

import com.example.kptc_smp.model.main.ActionTicket;
import com.example.kptc_smp.enums.ActionType;
import com.example.kptc_smp.exception.actionticket.ActionTicketExpireException;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActionTickerValidatorService {

    public void validateActionTicket(ActionTicket actionTicket, ActionType expectedType) {
        if (!isActionTicketOfType(actionTicket, expectedType)) {
            throw new ActionTicketNotFoundException();
        }
        if (isActionTicketExpired(actionTicket)) {
            throw new ActionTicketExpireException();
        }
    }

    public boolean isActionTicketOfType(ActionTicket actionTicket, ActionType expectedType) {
        return actionTicket.getActionType().equals(expectedType);
    }

    public boolean isActionTicketExpired(ActionTicket actionTicket) {
        return actionTicket.getExpiresAt().isBefore(LocalDateTime.now());
    }

}
