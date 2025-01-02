package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.Token;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.repository.main.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Optional<Token> findByTokenUUID(UUID tokenUUID) {
        return tokenRepository.findByTokenUUID(tokenUUID);
    }

    public void createNewTokenVersion(User user, UUID tokenUUID){
        Token token = new Token();
        token.setUser(user);
        token.setTokenUUID(tokenUUID);
        tokenRepository.save(token);
    }

    public void save(Token token) {
        tokenRepository.save(token);
    }
}
