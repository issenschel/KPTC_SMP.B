package com.example.kptc_smp.service;

import com.example.kptc_smp.entity.User;
import com.example.kptc_smp.exception.UserNotFountException;
import com.example.kptc_smp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username){
        User user = findByUsername(username).orElseThrow(UserNotFountException::new);

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
}
