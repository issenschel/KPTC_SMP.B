package com.example.kptc_smp.service.main;


import com.example.kptc_smp.dto.ActionTicketDto;
import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.TokenDto;
import com.example.kptc_smp.dto.email.CodeDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.entity.main.ActionTicket;
import com.example.kptc_smp.entity.main.EmailVerification;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.email.CodeExpireException;
import com.example.kptc_smp.exception.email.CodeValidationException;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.email.EmailNotFoundException;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.image.ImageNotFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final JwtTokenUtils jwtTokenUtils;
    private final EmailVerificationService emailVerificationService;
    private final UserDataTokenService userDataTokenService;
    private final AuthMeService authMeService;
    private final ImageService imageService;
    private final PasswordService passwordService;
    private final ActionTicketService actionTicketService;

    @Value("${google.drive.folder.image.profile.id}")
    private String imageProfileFolderId;

    public UserInformationDto getData() {
        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> new UserInformationDto(user.getId(), user.getUsername(), user.getUserInformation().getEmail())
        ).orElseThrow(UserNotFoundException::new);
    }

    public ResponseDto getImageName() {
        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    String imageName = user.getUserInformation().getImageName();
                    if (imageName != null) {
                        return new ResponseDto(imageName);
                    }
                    throw new ImageNotFoundException();
                }
        ).orElseThrow(UserNotFoundException::new);
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
        throw new ImageException();
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
                    return updateAndGenerateToken(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public TokenDto changeEmail(EmailChangeDto emailChangeDto) {
        return userService.findWithInfoAndTokenAndTicketByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    ActionTicket actionTicket = actionTicketService.getValidateActionTicket(user.getActionTicket(),emailChangeDto.getActionTicket());
                    userInformationService.findByEmail(emailChangeDto.getEmail()).ifPresent(t -> {throw new EmailFoundException();});
                    EmailVerification emailVerification = getValidatedEmailVerification(emailChangeDto.getEmail(), emailChangeDto.getCode());
                    emailVerificationService.delete(emailVerification);
                    actionTicketService.delete(actionTicket);
                    user.getUserInformation().setEmail(emailChangeDto.getEmail());
                    userInformationService.save(user.getUserInformation());
                    return updateAndGenerateToken(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public ActionTicketDto verifyCurrentEmailCode(CodeDto codeDto){
        return userService.findWithInfoAndDataTokenByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    EmailVerification emailVerification = getValidatedEmailVerification(user.getUserInformation().getEmail(), codeDto.getCode());
                    ActionTicket actionTicket = actionTicketService.createActionTicket(user);
                    emailVerificationService.delete(emailVerification);
                    return new ActionTicketDto(actionTicket.getTicket());
                }).orElseThrow(UserNotFoundException::new);
    }

    private TokenDto updateAndGenerateToken(User user) {
        UUID tokenUUID = UUID.randomUUID();
        user.getUserDataToken().setTokenUUID(tokenUUID);
        userDataTokenService.save(user.getUserDataToken());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenUtils.generateToken(authentication, tokenUUID);
        return new TokenDto(token);
    }

    private EmailVerification getValidatedEmailVerification (String email, String code) {
        EmailVerification emailVerification = emailVerificationService.findByEmail(email).orElseThrow(EmailNotFoundException::new);
        if(!emailVerificationService.validateCode(emailVerification, code)){
            throw new CodeValidationException();
        } else if (emailVerificationService.isExpired(emailVerification)) {
            throw new CodeExpireException();
        }
        return emailVerification;
    }

    private String updateOrUploadImage(MultipartFile image, User user){
        if (user.getUserInformation().getImageName() != null) {
            return updateImage(image, user.getUserInformation().getImageName());
        } else {
            return imageService.uploadImage(image,imageProfileFolderId);
        }
    }

    private String updateImage(MultipartFile image, String oldImageName) {
        imageService.deleteOldImage(oldImageName);
        return imageService.uploadImage(image,imageProfileFolderId);
    }
}
