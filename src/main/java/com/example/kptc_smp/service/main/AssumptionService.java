package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.Assumption;
import com.example.kptc_smp.repository.main.AssumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssumptionService{
    private final AssumptionRepository assumptionRepository;

        @Transactional
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

    public boolean validateCode(String email, String code) {
        return assumptionRepository.findByEmail(email).filter(assumption -> assumption.getCode().equals(code)).isPresent();
    }

    public void delete(Assumption assumption){
        assumptionRepository.delete(assumption);
    }

    public void deleteByEmail(String email) {
            assumptionRepository.deleteByEmail(email);
    }

    public Optional<Assumption> findByEmail(String email){
        return assumptionRepository.findByEmail(email);
    }
}
