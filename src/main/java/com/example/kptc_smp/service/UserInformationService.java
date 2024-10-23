package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.RegistrationUserDto;
import com.example.kptc_smp.entitys.User;
import com.example.kptc_smp.entitys.UserInformation;
import com.example.kptc_smp.repositories.UserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInformationService {
    private final UserInformationRepository userInformationRepository;

    @Transactional
    public UserInformation createNewUserDetails(RegistrationUserDto registrationUserDto, User user){
        UserInformation userInformation = new UserInformation();
        userInformation.setEmail(registrationUserDto.getEmail());
        userInformation.setMinecraftName(registrationUserDto.getMinecraftName());
        userInformation.setVersionId(UUID.randomUUID().toString());
        userInformation.setUser(user);
        return userInformationRepository.save(userInformation);
    }

    public Optional<UserInformation> findByEmail(String email) {
        return userInformationRepository.findByEmail(email);
    }

    public Optional<UserInformation> findByMinecraftName(String minecraftName) {
        return userInformationRepository.findByMinecraftName(minecraftName);
    }

    public Optional<UserInformation> findByVersionId(String versionId) {
        return userInformationRepository.findByVersionId(versionId);
    }

    public void save(UserInformation userInformation) {
        userInformationRepository.save(userInformation);
    }
}
