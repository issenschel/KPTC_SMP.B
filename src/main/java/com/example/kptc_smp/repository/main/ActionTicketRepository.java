package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.ActionTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionTicketRepository extends CrudRepository<ActionTicket, Integer> {

    Optional<ActionTicket> findByTicket(String token);

}
