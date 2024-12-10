package com.example.kptc_smp.service;


import com.example.kptc_smp.dto.*;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.LoginChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.entity.User;
import com.example.kptc_smp.exception.PhotoException;
import com.example.kptc_smp.exception.UserNotFountException;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AssumptionService assumptionService;
    private final TokenVersionService tokenVersionService;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public ResponseDto changeLogin(LoginChangeDto loginChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> {
                    userService.findByUsername(loginChangeDto.getNewUsername()).ifPresent(u -> {throw new UserNotFountException();});
                    if(!passwordEncoder.matches(loginChangeDto.getPassword(), us.getPassword())){
                        throw new UserNotFountException();
                    }
                    us.setUsername(loginChangeDto.getNewUsername());
                    userService.saveUser(us);
                    return updateAndGenerateToken(us);
                }).orElseThrow(UserNotFountException::new);
    }

    @Transactional
    public ResponseDto changePassword(PasswordChangeDto passwordChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> {
                    if(!passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword())){
                        throw new UserNotFountException();
                    }
                    if(!passwordEncoder.matches(passwordChangeDto.getOldPassword(), us.getPassword())){
                        throw new UserNotFountException();
                    };
                    us.setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));
                    userService.saveUser(us);
                    return updateAndGenerateToken(us);
                }).orElseThrow(UserNotFountException::new);
    }

    @Transactional
    public ResponseDto changeEmail(EmailChangeDto emailChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(us -> {
            userInformationService.findByEmail(emailChangeDto.getEmail()).ifPresent(u -> {throw new UserNotFountException();});
            if(assumptionService.validateCode(user.get().getUserInformation().getEmail(), emailChangeDto.getCode())) {
                throw new UserNotFountException();
            }
            assumptionService.findByEmail(us.getUserInformation().getEmail()).ifPresent(assumptionService::delete);
            us.getUserInformation().setEmail(emailChangeDto.getEmail());
            userInformationService.save(us.getUserInformation());
            return updateAndGenerateToken(us);
        }).orElseThrow(UserNotFountException::new);
    }

    @Transactional
    public ResponseDto changePhoto(MultipartFile photo) {
        if (photo != null && photo.getContentType() != null && !photo.getContentType().matches("image/.*")) {
            Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            return user.map(us -> {
                String uuidFile = UUID.randomUUID().toString();
                String result = uuidFile + "." + photo.getOriginalFilename();
                try {
                    if(us.getUserInformation().getPhoto() != null){
                        Files.delete(Path.of(uploadPath+"/"+us.getUserInformation().getPhoto()));
                    }
                    photo.transferTo(new File(uploadPath+"/"+ result));
                } catch (IOException e) {
                    throw new PhotoException();
                }
                us.getUserInformation().setPhoto(result);
                userInformationService.save(us.getUserInformation());
                return new ResponseDto("Успешное изменение фотографии");
            }).orElseThrow(UserNotFountException::new);
        }
        throw new PhotoException();
    }

    public UserInformationDto getData(){
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> new UserInformationDto(us.getId(), us.getUsername(), us.getUserInformation().getEmail(),
                        us.getUserInformation().getMinecraftName())
        ).orElseThrow(UserNotFountException::new);
    }

    public ResponseDto getPhoto(){
        return new ResponseDto(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> user.getUserInformation().getPhoto()
        ).orElseThrow( UserNotFountException::new));
    }

    private ResponseDto updateAndGenerateToken(User user) {
        String versionId = UUID.randomUUID().toString();
        user.getTokenVersion().setVersion(versionId);
        tokenVersionService.save(user.getTokenVersion());
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails, versionId);
        return new ResponseDto(token);
    }
}
