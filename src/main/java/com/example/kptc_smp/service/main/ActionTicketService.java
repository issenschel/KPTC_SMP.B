package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.ActionTicket;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.ActionTicketExpireException;
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

    public void isTicketExpired(ActionTicket actionTicket) {
        if(actionTicket == null || actionTicket.getExpiresAt().isAfter(LocalDateTime.now())){
            throw new ActionTicketExpireException();
        };
    }

    private String generateTicket() {
        return String.valueOf(System.currentTimeMillis() + Math.random());
    }
}
