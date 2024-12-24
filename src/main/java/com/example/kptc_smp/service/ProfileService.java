package com.example.kptc_smp.service;


import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.entity.postgreSQL.User;
import com.example.kptc_smp.exception.UserNotFoundException;
import com.example.kptc_smp.exception.profile.CodeValidationException;
import com.example.kptc_smp.exception.profile.PasswordValidationException;
import com.example.kptc_smp.exception.profile.PhotoException;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final AuthMeService authMeService;
    private final FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public ResponseDto changePassword(PasswordChangeDto passwordChangeDto) {
        Optional<User> user = userService.findWithTokenVersionByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> {
                    if (!passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword())) {
                        throw new PasswordValidationException();
                    }
                    checkPassword(passwordChangeDto.getOldPassword(), us.getPassword());
                    String password = passwordEncoder.encode(passwordChangeDto.getPassword());
                    us.setPassword(password);
                    authMeService.updatePassword(us.getUsername(), password);
                    userService.saveUser(us);
                    return updateAndGenerateToken(us);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public ResponseDto changeEmail(EmailChangeDto emailChangeDto) {
        Optional<User> user = userService.findWithUserInformationAndTokenVersionByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(us -> {
            if (!assumptionService.validateCode(user.get().getUserInformation().getEmail(), emailChangeDto.getCode())) {
                throw new CodeValidationException();
            }
            assumptionService.findByEmail(us.getUserInformation().getEmail()).ifPresent(assumptionService::delete);
            us.getUserInformation().setEmail(emailChangeDto.getEmail());
            userInformationService.save(us.getUserInformation());
            return updateAndGenerateToken(us);
        }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public ResponseDto changePhoto(MultipartFile photo) {
        if (photo != null && photo.getContentType() != null && photo.getContentType().matches("image/.*")) {
            Optional<User> user = userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            return user.map(us -> {
                us.getUserInformation().setPhoto(fileService.updatePhoto(photo, us.getUserInformation().getPhoto()));
                userInformationService.save(us.getUserInformation());
                return new ResponseDto("Успешное изменение фотографии");
            }).orElseThrow(UserNotFoundException::new);
        }
        throw new PhotoException();
    }

    public UserInformationDto getData() {
        Optional<User> user = userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> new UserInformationDto(us.getId(), us.getUsername(), us.getUserInformation().getEmail())
        ).orElseThrow(UserNotFoundException::new);
    }

    public Resource getPhoto() {
        return (userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    String photo = user.getUserInformation().getPhoto();
                    Path path = Paths.get(uploadPath + "/" + photo);
                    try {
                        return new UrlResource(path.toUri());
                    } catch (MalformedURLException e) {
                        throw new PhotoException();
                    }
                }
        ).orElseThrow(UserNotFoundException::new));
    }

    private ResponseDto updateAndGenerateToken(User user) {
        String versionId = UUID.randomUUID().toString();
        user.getTokenVersion().setVersion(versionId);
        tokenVersionService.save(user.getTokenVersion());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenUtils.generateToken(authentication, versionId);
        return new ResponseDto(token);
    }

    private void checkPassword(String password, String password2) {
        if (!passwordEncoder.matches(password, password2)) {
            throw new PasswordValidationException();
        }
    }
}
