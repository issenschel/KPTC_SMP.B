package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.registration.RegistrationUserDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserInformation;
import com.example.kptc_smp.repository.main.UserInformationRepository;
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
        userInformation.setUser(user);
        return userInformationRepository.save(userInformation);
    }

    public Optional<UserInformation> findByEmail(String email) {
        return userInformationRepository.findByEmail(email);
    }

    public void save(UserInformation userInformation) {
        userInformationRepository.save(userInformation);
    }
}
