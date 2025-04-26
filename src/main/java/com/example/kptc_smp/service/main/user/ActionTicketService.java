package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.entity.main.ActionTicket;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.actionticket.ActionTicketExpireException;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import com.example.kptc_smp.repository.main.ActionTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActionTicketService {
    private final ActionTicketRepository actionTicketRepository;

    public ActionTicket createActionTicket(User user) {
        ActionTicket actionTicket = new ActionTicket();
        actionTicket.setUser(user);
        actionTicket.setTicket(generateTicket());
        actionTicket.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        return actionTicketRepository.save(actionTicket);
    }

    public ActionTicket getValidateActionTicket(ActionTicket actionTicket, String ticket) {
        if (actionTicket == null || !actionTicket.getTicket().equals(ticket)) {
            throw new ActionTicketNotFoundException();
        }
        isTicketExpired(actionTicket);
        return actionTicket;
    }

    public void isTicketExpired(ActionTicket actionTicket) {
        if (actionTicket.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ActionTicketExpireException();
        }
    }

    public void delete(ActionTicket actionTicket) {
        actionTicketRepository.delete(actionTicket);
    }

    private String generateTicket() {
        return String.valueOf(System.currentTimeMillis() + Math.random());
    }

}
