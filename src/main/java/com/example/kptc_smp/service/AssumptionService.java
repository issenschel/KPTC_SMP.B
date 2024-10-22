package com.example.kptc_smp.service;

import com.example.kptc_smp.entitys.Assumption;
import com.example.kptc_smp.repositories.AssumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssumptionService{
    private final AssumptionRepository assumptionRepository;

    public void saveOrUpdate(String email, String code){
        Optional<Assumption> assumptionOptional = assumptionRepository.findByEmail(email);
        if (assumptionOptional.isPresent()) {
            Assumption assumption = assumptionOptional.get();
            assumption.setCode(code);
            assumptionRepository.save(assumption);
        } else {
            createNewAssumption(email, code);
        }
    }

    public void createNewAssumption(String email, String code) {
        Assumption assumption = new Assumption();
        assumption.setEmail(email);
        assumption.setCode(code);
        assumptionRepository.save(assumption);

    }

    public void delete(String email){
        assumptionRepository.delete(assumptionRepository.findByEmail(email).get());
    }
}
