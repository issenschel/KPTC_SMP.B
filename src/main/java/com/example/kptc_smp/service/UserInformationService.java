package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.registration.RegistrationUserDto;
import com.example.kptc_smp.entity.User;
import com.example.kptc_smp.entity.UserInformation;
import com.example.kptc_smp.repository.UserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInformationService {
    private final UserInformationRepository userInformationRepository;

    public UserInformation createNewUserInformation(RegistrationUserDto registrationUserDto, User user) {
        UserInformation userInformation = new UserInformation();
        userInformation.setEmail(registrationUserDto.getEmail());
        userInformation.setMinecraftName(registrationUserDto.getMinecraftName());
        userInformation.setUser(user);
        return userInformationRepository.save(userInformation);
    }

    public Optional<UserInformation> findByEmail(String email) {
        return userInformationRepository.findByEmail(email);
    }

    public Optional<UserInformation> findByMinecraftName(String minecraftName) {
        return userInformationRepository.findByMinecraftName(minecraftName);
    }

    public void save(UserInformation userInformation) {
        userInformationRepository.save(userInformation);
    }
}
