package com.example.kptc_smp.service.main.user;


import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.TokenDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserAccountDetailsDto;
import com.example.kptc_smp.dto.profile.UserProfileDto;
import com.example.kptc_smp.entity.main.ActionTicket;
import com.example.kptc_smp.entity.main.EmailVerification;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.enums.ImageType;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.image.ImageInvalidFormatException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.email.EmailVerificationService;
import com.example.kptc_smp.service.main.auth.PasswordService;
import com.example.kptc_smp.service.main.image.ImageService;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final EmailVerificationService emailVerificationService;
    private final UserDataTokenService userDataTokenService;
    private final AuthMeService authMeService;
    private final ImageService imageService;
    private final PasswordService passwordService;
    private final ActionTicketService actionTicketService;

    public UserAccountDetailsDto getUserAccountDetails() {
        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> new UserAccountDetailsDto(user.getId(), user.getUsername(), user.getUserInformation().getEmail(),
                        user.getUserInformation().getRegistrationDate())
        ).orElseThrow(UserNotFoundException::new);
    }

    public UserProfileDto getUserProfileInfo() {
        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                        user -> new UserProfileDto(user.getUsername(),
                                imageService.getImageUrl(ImageType.PROFILE, user.getUserInformation().getImageName())))
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public TokenDto changePassword(PasswordChangeDto passwordChangeDto) {
        return userService.findWithTokenVersionByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    passwordService.validatePasswordEquals(passwordChangeDto.getPassword(), passwordChangeDto.getConfirmPassword());
                    passwordService.validateEncodedPasswordMatch(passwordChangeDto.getOldPassword(), user.getPassword());
                    String password = passwordService.encodePassword(passwordChangeDto.getPassword());
                    user.setPassword(password);
                    authMeService.updatePassword(user.getUsername(), password);
                    userService.saveUser(user);
                    return userDataTokenService.updateAndGenerateToken(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public TokenDto changeEmail(EmailChangeDto emailChangeDto) {
        return userService.findWithInfoAndTokenAndTicketByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    ActionTicket actionTicket = actionTicketService.getValidateActionTicket(user.getActionTicket(), emailChangeDto.getActionTicket());
                    userInformationService.findByEmail(emailChangeDto.getEmail()).ifPresent(t -> {
                        throw new EmailFoundException();
                    });
                    EmailVerification emailVerification = emailVerificationService.getValidatedEmailVerification
                            (emailChangeDto.getEmail(), emailChangeDto.getCode());
                    emailVerificationService.delete(emailVerification);
                    actionTicketService.delete(actionTicket);
                    user.getUserInformation().setEmail(emailChangeDto.getEmail());
                    userInformationService.save(user.getUserInformation());
                    return userDataTokenService.updateAndGenerateToken(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public ResponseDto changeImage(MultipartFile image) {
        if (imageService.isValidImage(image)) {
            return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                    user -> {
                        String imageName = updateOrUploadImage(image, user);
                        user.getUserInformation().setImageName(imageName);
                        userInformationService.save(user.getUserInformation());
                        return new ResponseDto("Успешное изменение фотографии");
                    }).orElseThrow(UserNotFoundException::new);
        }
        throw new ImageInvalidFormatException();
    }

    private String updateOrUploadImage(MultipartFile image, User user) {
        if (user.getUserInformation().getImageName() != null) {
            return imageService.updateImage(ImageType.PROFILE,image, user.getUserInformation().getImageName());
        }
        return imageService.uploadImage(ImageType.PROFILE,image);
    }

}
