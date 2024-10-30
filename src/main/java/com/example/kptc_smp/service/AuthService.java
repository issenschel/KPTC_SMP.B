package com.example.kptc_smp.service;
import com.example.kptc_smp.dto.JwtRequestDto;
import com.example.kptc_smp.dto.RegistrationUserDto;
import com.example.kptc_smp.dto.UserInformationDto;
import com.example.kptc_smp.entitys.User;
import com.example.kptc_smp.entitys.UserInformation;
import com.example.kptc_smp.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final AssumptionService assumptionService;


    public String createAuthToken(@RequestBody JwtRequestDto authRequest) throws BadCredentialsException {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
            Optional<User> user = userService.findByUsername(authRequest.getUsername());
            String versionId = user.get().getUserInformation().getVersionId();
            return jwtTokenUtils.generateToken(userDetails, versionId);
    }

    @Transactional
    public UserInformationDto createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
            User user = userService.createNewUser(registrationUserDto.getUsername(), registrationUserDto.getPassword());
            UserInformation userInformation = userInformationService.createNewUserDetails(registrationUserDto, user);
            assumptionService.findByEmail(registrationUserDto.getEmail()).ifPresent(assumptionService::delete);
            return new UserInformationDto(userInformation.getId(), userInformation.getUser().getUsername(),
                    userInformation.getEmail(), userInformation.getMinecraftName());
    }

}
