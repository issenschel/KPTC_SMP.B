package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.model.main.ActionTicket;
import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.enums.ActionType;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import com.example.kptc_smp.repository.main.ActionTicketRepository;
import com.example.kptc_smp.service.main.validator.ActionTickerValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActionTicketService {
    private final ActionTicketRepository actionTicketRepository;
    private final ActionTickerValidatorService actionTickerValidatorService;

    @Value("${action.ticket.expiration.time}")
    private int actionTicketExpirationTime;

    public ActionTicket createActionTicket(User user, ActionType actionType) {
        ActionTicket actionTicket = new ActionTicket();
        actionTicket.setUser(user);
        actionTicket.setTicket(generateTicket());
        actionTicket.setActionType(actionType);
        actionTicket.setExpiresAt(LocalDateTime.now().plusMinutes(actionTicketExpirationTime));
        return actionTicketRepository.save(actionTicket);
    }

    public ActionTicket updateActionTicket(ActionTicket actionTicket) {
        actionTicket.setTicket(generateTicket());
        actionTicket.setExpiresAt(LocalDateTime.now().plusMinutes(actionTicketExpirationTime));

        return actionTicket;
    }

    public ActionTicket updateOrCreateActionTicket(User user,ActionType actionType) {
        ActionTicket actionTicket = user.getActionTickets().stream()
                .filter(t -> actionType.equals(t.getActionType()))
                .findFirst()
                .orElse(null);
        if (actionTicket != null) {
            return updateActionTicket(actionTicket);
        } else {
            return createActionTicket(user,actionType);
        }
    }

    public Optional<ActionTicket> findByTicket(String ticket) {
        return actionTicketRepository.findByTicket(ticket);
    }

    public ActionTicket findValidActionTicketByType(User user, String ticket, ActionType expectedType) {
        ActionTicket actionTicket = user.getActionTickets().stream()
                .filter(t -> t.getTicket().equals(ticket))
                .findFirst()
                .orElseThrow(ActionTicketNotFoundException::new);

        actionTickerValidatorService.validateActionTicket(actionTicket, expectedType);

        return actionTicket;
    }

    public void delete(ActionTicket actionTicket) {
        actionTicketRepository.delete(actionTicket);
    }

    private String generateTicket() {
        return UUID.randomUUID() + "-" + System.currentTimeMillis();
    }

}
