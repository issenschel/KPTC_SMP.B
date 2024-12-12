package com.example.kptc_smp.service.minecraft;

import com.example.kptc_smp.entity.mySQL.AuthMe;
import com.example.kptc_smp.entity.postgreSQL.User;
import com.example.kptc_smp.repository.mySQL.AuthMeRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMeService {
    public final AuthMeRepository authMeRepository;
    private final HttpServletRequest request;

    public void createAuthMe(User user){
        AuthMe authMe = new AuthMe();
        authMe.setUsername(user.getUsername());
        authMe.setRealName(user.getUsername());
        authMe.setPassword(user.getPassword());
        authMe.setRegDate(System.currentTimeMillis());
        authMe.setRegIP(request.getRemoteAddr());
        authMeRepository.save(authMe);
    }
}
