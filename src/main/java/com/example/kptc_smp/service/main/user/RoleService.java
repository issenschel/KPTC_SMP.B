package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.model.main.Role;
import com.example.kptc_smp.exception.role.RoleNotFoundException;
import com.example.kptc_smp.repository.main.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Value("${default.user.role.name}")
    private String defaultUserRoleName;

    public Role getUserRole(){
        return roleRepository.findByName(defaultUserRoleName).orElseThrow(RoleNotFoundException::new);
    }
}
