package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.AuthToken;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.repository.main.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    private final AuthTokenRepository authTokenRepository;

    public Optional<AuthToken> findByTokenUUID(UUID tokenUUID) {
        return authTokenRepository.findByTokenUUID(tokenUUID);
    }

    public void createAuthToken(User user, UUID tokenUUID){
        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken.setTokenUUID(tokenUUID);
        authTokenRepository.save(authToken);
    }

    public void save(AuthToken authToken) {
        authTokenRepository.save(authToken);
    }
}
