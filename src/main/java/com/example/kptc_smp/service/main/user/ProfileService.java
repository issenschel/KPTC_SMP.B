package com.example.kptc_smp.service.main.user;


import com.example.kptc_smp.dto.JwtTokenPairDto;
import com.example.kptc_smp.dto.auth.TokenDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserAccountDetailsDto;
import com.example.kptc_smp.dto.profile.UserProfileDto;
import com.example.kptc_smp.entity.main.*;
import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.auth.PasswordService;
import com.example.kptc_smp.service.main.email.EmailVerificationService;
import com.example.kptc_smp.service.main.image.ImageStorageService;
import com.example.kptc_smp.service.main.image.ImageValidator;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final EmailVerificationService emailVerificationService;
    private final UserDataTokenService userDataTokenService;
    private final AuthMeService authMeService;
    private final ImageStorageService imageStorageService;
    private final ImageValidator imageValidator;
    private final PasswordService passwordService;
    private final ActionTicketService actionTicketService;
    private final UserSessionService userSessionService;
    private final JwtTokenUtils jwtTokenUtils;
    private final HttpServletRequest request;

    public UserAccountDetailsDto getUserAccountDetails() {
        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> new UserAccountDetailsDto(user.getId(), user.getUsername(),
                        user.getUserInformation().getEmail(), user.getUserInformation().getRegistrationDate())
        ).orElseThrow(UserNotFoundException::new);
    }

    public UserProfileDto getUserProfileInfo() {
        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> {
                    UUID imageId = Optional.ofNullable(user.getUserInformation())
                            .map(UserInformation::getImageRegistry)
                            .map(ImageRegistry::getId)
                            .orElse(null);
                    return new UserProfileDto(user.getUsername(), imageStorageService.getImageUrl(imageId));
                })
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(transactionManager = "chainedTransactionManager")
    public JwtTokenPairDto changePassword(PasswordChangeDto passwordChangeDto) {
        return userService.findWithTokenVersionByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    passwordService.validatePasswordEquals(passwordChangeDto.getPassword(), passwordChangeDto.getConfirmPassword());
                    passwordService.validateEncodedPasswordMatch(passwordChangeDto.getOldPassword(), user.getPassword());

                    String password = passwordService.encodePassword(passwordChangeDto.getPassword());

                    user.setPassword(password);
                    authMeService.updatePassword(user.getUsername(), password);

                    return getJwtTokenPairDto(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public JwtTokenPairDto changeEmail(EmailChangeDto emailChangeDto) {
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

                    return getJwtTokenPairDto(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    private JwtTokenPairDto getJwtTokenPairDto(User user) {
        UserDataToken userDataToken = userDataTokenService.updateUserDataToken(user);

        userSessionService.deleteAllSessionsByUser(user);
        UserSession userSession = userSessionService.createSession(user);

        String accessToken = jwtTokenUtils.generateAccessToken(userDataToken.getTokenUUID());

        return new JwtTokenPairDto(accessToken, userSession.getRefreshToken());
    }

    @Transactional
    public UserProfileDto changeImage(MultipartFile image) {
        imageValidator.validateImage(image);

        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    ImageRegistry imageRegistry = imageStorageService.updateOrUploadImage(image, user);
                    user.getUserInformation().setImageRegistry(imageRegistry);

                    return new UserProfileDto(user.getUsername(), imageStorageService.getImageUrl(imageRegistry.getId()));
                }).orElseThrow(UserNotFoundException::new);
    }


}
