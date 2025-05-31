package com.example.kptc_smp.service.minecraft;

import com.example.kptc_smp.model.minecraft.AuthMe;
import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.repository.minecraft.AuthMeRepository;
import com.example.kptc_smp.service.main.user.ClientInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMeService {
    private final AuthMeRepository authMeRepository;
    private final ClientInfoService clientInfoService;

    public void createAuthMe(User user){
        AuthMe authMe = new AuthMe();

        authMe.setUsername(user.getUsername());
        authMe.setRealName(user.getUsername());
        authMe.setPassword(user.getPassword());
        authMe.setRegistrationDate(System.currentTimeMillis());
        authMe.setRegIP(clientInfoService.getClientIp());

        authMeRepository.save(authMe);
    }

    public void updatePassword(String username,String password){
        AuthMe authMe = authMeRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        authMe.setPassword(password);
        authMe.setHasSession(false);
    }
}
