package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserDataToken;
import com.example.kptc_smp.repository.main.UserDataTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDataTokenService {
    private final UserDataTokenRepository userDataTokenRepository;

    public Optional<UserDataToken> findByTokenUUID(UUID tokenUUID) {
        return userDataTokenRepository.findByTokenUUID(tokenUUID);
    }

    public void createUserDataToken(User user, UUID tokenUUID) {
        UserDataToken userDataToken = new UserDataToken();

        userDataToken.setUser(user);
        userDataToken.setTokenUUID(tokenUUID);

        userDataTokenRepository.save(userDataToken);
    }

    public UserDataToken updateUserDataToken(User user) {
        UUID tokenUUID = UUID.randomUUID();

        UserDataToken userDataToken = user.getUserDataToken();
        userDataToken.setTokenUUID(tokenUUID);

        return userDataToken;
    }

    public void save(UserDataToken userDataToken) {
        userDataTokenRepository.save(userDataToken);
    }
}
