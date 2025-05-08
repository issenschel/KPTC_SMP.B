package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.entity.main.ActionTicket;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.enums.ActionType;
import com.example.kptc_smp.exception.actionticket.ActionTicketExpireException;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import com.example.kptc_smp.repository.main.ActionTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActionTicketService {
    private final ActionTicketRepository actionTicketRepository;

    public ActionTicket createActionTicket(User user, ActionType actionType) {
        ActionTicket actionTicket = new ActionTicket();
        actionTicket.setUser(user);
        actionTicket.setTicket(generateTicket());
        actionTicket.setActionType(actionType);
        actionTicket.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        return actionTicketRepository.save(actionTicket);
    }

    public ActionTicket updateActionTicket(ActionTicket actionTicket) {
        actionTicket.setTicket(generateTicket());
        actionTicket.setExpiresAt(LocalDateTime.now().plusMinutes(10));

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

        validateActionTicket(actionTicket, expectedType);

        return actionTicket;
    }

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

    public void delete(ActionTicket actionTicket) {
        actionTicketRepository.delete(actionTicket);
    }

    private String generateTicket() {
        return UUID.randomUUID() + "-" + System.currentTimeMillis();
    }

}
