package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.dto.auth.TokenDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserDataToken;
import com.example.kptc_smp.repository.main.UserDataTokenRepository;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDataTokenService {
    private final UserDataTokenRepository userDataTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public Optional<UserDataToken> findByTokenUUID(UUID tokenUUID) {
        return userDataTokenRepository.findByTokenUUID(tokenUUID);
    }

    public void createUserDataToken(User user, UUID tokenUUID) {
        UserDataToken userDataToken = new UserDataToken();
        userDataToken.setUser(user);
        userDataToken.setTokenUUID(tokenUUID);
        userDataTokenRepository.save(userDataToken);
    }

    public TokenDto updateAndGenerateToken(User user) {
        UUID tokenUUID = UUID.randomUUID();
        user.getUserDataToken().setTokenUUID(tokenUUID);
        save(user.getUserDataToken());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenUtils.generateToken(authentication, tokenUUID);
        return new TokenDto(token);
    }

    public void save(UserDataToken userDataToken) {
        userDataTokenRepository.save(userDataToken);
    }
}
