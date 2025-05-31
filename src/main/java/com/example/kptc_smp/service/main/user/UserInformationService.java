package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.dto.auth.RegistrationUserRequestDto;
import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.model.main.UserInformation;
import com.example.kptc_smp.repository.main.UserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInformationService {

    private final UserInformationRepository userInformationRepository;

    public UserInformation createNewUserInformation(RegistrationUserRequestDto registrationUserRequestDto, User user) {
        UserInformation userInformation = new UserInformation();
        userInformation.setEmail(registrationUserRequestDto.getEmail());
        userInformation.setUser(user);
        userInformation.setRegistrationDate(LocalDate.now());
        return userInformationRepository.save(userInformation);
    }

    public Optional<UserInformation> findByEmail(String email) {
        return userInformationRepository.findByEmail(email);
    }

    public Optional<UserInformation> findWithUserByEmail(String email) {
        return userInformationRepository.findWithUserByEmail(email);
    }

    public void save(UserInformation userInformation) {
        userInformationRepository.save(userInformation);
    }
}
