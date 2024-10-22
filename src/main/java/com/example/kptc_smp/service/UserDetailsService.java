package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.RegistrationUserDto;
import com.example.kptc_smp.entitys.UserDetails;
import com.example.kptc_smp.repositories.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
    private final UserDetailsRepository userDetailsRepository;
    private final UserService userService;

    @Transactional
    public UserDetails createNewUserDetails(RegistrationUserDto registrationUserDto){
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail(registrationUserDto.getEmail());
        userDetails.setMinecraftName(registrationUserDto.getMinecraftName());
        userDetails.setVersionId(UUID.randomUUID().toString());
        userDetails.setUser(userService.createNewUser(registrationUserDto.getUsername(), registrationUserDto.getPassword()));
        return userDetailsRepository.save(userDetails);
    }

    public Optional<UserDetails> findByEmail(String email) {
        return userDetailsRepository.findByEmail(email);
    }

    public Optional<UserDetails> findByMinecraftName(String minecraftName) {
        return userDetailsRepository.findByMinecraftName(minecraftName);
    }
}
