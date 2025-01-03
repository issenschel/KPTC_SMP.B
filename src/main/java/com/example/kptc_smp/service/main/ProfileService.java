package com.example.kptc_smp.service.main;


import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.image.ImageNotFoundException;
import com.example.kptc_smp.exception.email.CodeValidationException;
import com.example.kptc_smp.exception.profile.PasswordValidationException;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final EmailVerificationService emailVerificationService;
    private final AuthTokenService authTokenService;
    private final AuthMeService authMeService;
    private final ImageService imageService;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public ResponseDto changePassword(PasswordChangeDto passwordChangeDto) {
        Optional<User> user = userService.findWithTokenVersionByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> {
                    if (!passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword())) {
                        throw new PasswordValidationException("Новый пароль и подтверждение пароля не совпадают");
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
            if (!emailVerificationService.validateCode(user.get().getUserInformation().getEmail(), emailChangeDto.getCode())) {
                throw new CodeValidationException();
            }
            userInformationService.findByEmail(emailChangeDto.getEmail()).ifPresent(t -> {
                throw new EmailFoundException();
            });
            emailVerificationService.findByEmail(us.getUserInformation().getEmail()).ifPresent(emailVerificationService::delete);
            us.getUserInformation().setEmail(emailChangeDto.getEmail());
            userInformationService.save(us.getUserInformation());
            return updateAndGenerateToken(us);
        }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public ResponseDto changeImage(MultipartFile image) {
        if (image != null && image.getContentType() != null && image.getContentType().matches("image/.*")) {
            Optional<User> user = userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            return user.map(us -> {
                String imageName;
                if (us.getUserInformation().getImageName() == null) {
                    imageName = imageService.uploadImage(image);
                } else {
                    imageName = imageService.updateImage(image, us.getUserInformation().getImageName());
                }
                us.getUserInformation().setImageName(imageName);
                userInformationService.save(us.getUserInformation());
                return new ResponseDto("Успешное изменение фотографии");
            }).orElseThrow(UserNotFoundException::new);
        }
        throw new ImageException();
    }

    public UserInformationDto getData() {
        Optional<User> user = userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> new UserInformationDto(us.getId(), us.getUsername(), us.getUserInformation().getEmail())
        ).orElseThrow(UserNotFoundException::new);
    }

    public ResponseDto getImageName() {
        return (userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    String imageName = user.getUserInformation().getImageName();
                    if (imageName != null) {
                        return new ResponseDto(imageName);
                    }
                    throw new ImageNotFoundException();
                }
        ).orElseThrow(UserNotFoundException::new));
    }

    private ResponseDto updateAndGenerateToken(User user) {
        UUID tokenUUID = UUID.randomUUID();
        user.getAuthToken().setTokenUUID(tokenUUID);
        authTokenService.save(user.getAuthToken());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenUtils.generateToken(authentication, tokenUUID);
        return new ResponseDto(token);
    }

    private void checkPassword(String currentPassword, String inputAtPassword) {
        if (!passwordEncoder.matches(currentPassword, inputAtPassword)) {
            throw new PasswordValidationException();
        }
    }
}
