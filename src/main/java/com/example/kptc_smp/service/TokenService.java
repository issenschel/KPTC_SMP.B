package com.example.kptc_smp.service;

import com.example.kptc_smp.entity.postgreSQL.Token;
import com.example.kptc_smp.entity.postgreSQL.User;
import com.example.kptc_smp.repository.postgreSQL.TokenVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenVersionRepository tokenVersionRepository;

    public Optional<Token> findByTokenUUID(UUID tokenUUID) {
        return tokenVersionRepository.findByTokenUUID(tokenUUID);
    }

    public void createNewTokenVersion(User user, UUID tokenUUID){
        Token token = new Token();
        token.setUser(user);
        token.setTokenUUID(tokenUUID);
        tokenVersionRepository.save(token);
    }

    public void save(Token token) {
        tokenVersionRepository.save(token);
    }
}
