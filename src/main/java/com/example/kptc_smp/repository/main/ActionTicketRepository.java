package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.ActionTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionTicketRepository extends CrudRepository<ActionTicket, Integer> {

}
