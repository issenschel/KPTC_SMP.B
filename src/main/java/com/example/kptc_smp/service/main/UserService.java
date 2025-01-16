package com.example.kptc_smp.service.main;

import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.repository.main.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username){
        User user = findWithAllDependenciesByUsername(username).orElseThrow(UserNotFoundException::new);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public User createNewUser(String name,String password) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findWithAllDependenciesByUsername(String username) {
        return userRepository.findWithRolesByUsername(username);
    }

    public Optional<User> findWithTokenVersionByUsername(String username) {
        return userRepository.findWithTokenVersionByUsername(username);
    }

    public Optional<User> findWithUserInformationByUsername(String username) {
        return userRepository.findWithUserInformationByUsername(username);
    }

    public Optional<User> findWithInfoAndDataTokenByUsername(String username) {
        return userRepository.findWithInfoAndDataTokenByUsername(username);
    }

    public Optional<User> findWithInfoAndTokenAndTicketByUsername(String username) {
        return userRepository.findWithInfoAndTokenAndTicketByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
