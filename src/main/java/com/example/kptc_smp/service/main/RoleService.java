package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.Role;
import com.example.kptc_smp.repository.main.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole(){
        return roleRepository.findByName("ROLE_USER").get();
    }
}
