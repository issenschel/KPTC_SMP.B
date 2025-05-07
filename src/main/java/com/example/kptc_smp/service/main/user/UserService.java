package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.repository.main.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public User createUser(String name, String password) {
        User user = new User();

        user.setUsername(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(List.of(roleService.getUserRole()));

        return userRepository.save(user);
    }

    public Optional<User> findWithTokenVersionByUsername(String username) {
        return userRepository.findWithTokenVersionByUsername(username);
    }

    public Optional<User> findWithUserInformationByUsername(String username) {
        return userRepository.findWithUserInformationByUsername(username);
    }

    public Optional<User> findWithInfoAndTokenAndTicketByUsername(String username) {
        return userRepository.findWithInfoAndTokenAndTicketByUsername(username);
    }

    public Optional<User> findWithSessionsAndTokenByUsername(String username) {
        return userRepository.findWithSessionsAndTokenByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
