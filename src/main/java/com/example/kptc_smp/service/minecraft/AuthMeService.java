package com.example.kptc_smp.service.minecraft;

import com.example.kptc_smp.entity.minecraft.AuthMe;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.repository.minecraft.AuthMeRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthMeService {
    private final AuthMeRepository authMeRepository;
    private final HttpServletRequest request;

    @Transactional(transactionManager = "mySQLTransactionManager")
    public void createAuthMe(User user){
        AuthMe authMe = new AuthMe();
        authMe.setUsername(user.getUsername());
        authMe.setRealName(user.getUsername());
        authMe.setPassword(user.getPassword());
        authMe.setRegistrationDate(System.currentTimeMillis());
        authMe.setRegIP(request.getRemoteAddr());
        authMeRepository.save(authMe);
    }

    @Transactional(transactionManager = "mySQLTransactionManager")
    public void updatePassword(String username,String password){
        AuthMe authMe = authMeRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        authMe.setPassword(password);
        authMeRepository.save(authMe);
    }
}
