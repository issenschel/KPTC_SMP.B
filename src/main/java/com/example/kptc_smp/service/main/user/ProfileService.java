package com.example.kptc_smp.service.main.user;


import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.JwtTokenPairResponseDto;
import com.example.kptc_smp.dto.image.ImageResponseDto;
import com.example.kptc_smp.dto.profile.*;
import com.example.kptc_smp.model.main.*;
import com.example.kptc_smp.enums.ActionType;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.service.main.auth.PasswordService;
import com.example.kptc_smp.service.main.email.EmailVerificationService;
import com.example.kptc_smp.service.main.image.ImageMapperService;
import com.example.kptc_smp.service.main.image.ImageStorageService;
import com.example.kptc_smp.service.main.validator.ImageValidatorService;
import com.example.kptc_smp.service.minecraft.AuthMeService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final EmailVerificationService emailVerificationService;
    private final AuthMeService authMeService;
    private final ImageStorageService imageStorageService;
    private final ImageValidatorService imageValidatorService;
    private final ImageMapperService imageMapperService;
    private final PasswordService passwordService;
    private final ActionTicketService actionTicketService;
    private final UserSessionService userSessionService;
    private final JwtTokenUtils jwtTokenUtils;
    private final ClientInfoService clientInfoService;


    @Value("${message.session.deleted.current}")
    private String currentSessionDeletedMessage;

    @Value("${message.session.deleted.all}")
    private String allSessionsDeletedMessage;


    public UserAccountDetailsResponseDto getUserAccountDetails() {
        UUID currentSessionId = (UUID) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> new UserAccountDetailsResponseDto(user.getId(), user.getUsername(),
                        user.getUserInformation().getEmail(), user.getUserInformation().getRegistrationDate(),currentSessionId)
        ).orElseThrow(UserNotFoundException::new);
    }

    public UserProfileResponseDto getUserProfileInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findWithUserInformationByUsername(authentication.getName())
                .map(user -> {
                    UUID imageId = Optional.ofNullable(user.getUserInformation())
                            .map(UserInformation::getImageRegistry)
                            .map(ImageRegistry::getId)
                            .orElse(null);
                    return new UserProfileResponseDto(user.getUsername(),
                            authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                            imageStorageService.getImageUrl(imageId));
                })
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(transactionManager = "chainedTransactionManager")
    public JwtTokenPairResponseDto changePassword(PasswordChangeRequestDto passwordChangeRequestDto) {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    passwordService.validatePasswordEquals(passwordChangeRequestDto.getPassword(), passwordChangeRequestDto.getConfirmPassword());
                    passwordService.validateEncodedPasswordMatch(passwordChangeRequestDto.getOldPassword(), user.getPassword());

                    String password = passwordService.encodePassword(passwordChangeRequestDto.getPassword());

                    user.setPassword(password);
                    authMeService.updatePassword(user.getUsername(), password);

                    return getJwtTokenPairDto(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public JwtTokenPairResponseDto changeEmail(EmailChangeRequestDto emailChangeRequestDto) {
        return userService.findWithInfoAndTicketByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    ActionTicket actionTicket = actionTicketService.findValidActionTicketByType
                            (user, emailChangeRequestDto.getActionTicket(), ActionType.EMAIL_CHANGE);
                    userInformationService.findByEmail(emailChangeRequestDto.getEmail())
                            .ifPresent(t -> {throw new EmailFoundException();});
                    EmailVerification emailVerification = emailVerificationService.findValidEmailVerification
                            (emailChangeRequestDto.getEmail(), emailChangeRequestDto.getCode());

                    emailVerificationService.delete(emailVerification);
                    actionTicketService.delete(actionTicket);
                    user.getUserInformation().setEmail(emailChangeRequestDto.getEmail());

                    return getJwtTokenPairDto(user);
                }).orElseThrow(UserNotFoundException::new);
    }

    private JwtTokenPairResponseDto getJwtTokenPairDto(User user) {
        userSessionService.deleteAllSessionsByUser(user);
        UserSession userSession = userSessionService.createSession(user);

        String accessToken = jwtTokenUtils.generateAccessToken(user.getUsername(),userSession.getId());

        return new JwtTokenPairResponseDto(userSession.getRefreshToken(),accessToken);
    }

    @Transactional
    public ImageResponseDto changeImage(MultipartFile image) {
        imageValidatorService.validateImage(image);

        return userService.findWithUserInformationByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    ImageRegistry imageRegistry = imageStorageService.updateOrUploadImage(image, user);
                    user.getUserInformation().setImageRegistry(imageRegistry);

                    return imageMapperService.toImageResponse(imageRegistry);
                }).orElseThrow(UserNotFoundException::new);
    }

    public List<SessionDataResponseDto> getAllSession(){
        return userService.findWithUserSessionsByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> userSessionService.getAllSessionsByUser(user).stream().map(
                        sessions -> new SessionDataResponseDto(sessions.getId(),sessions.getIpAddress(),
                                clientInfoService.parseUserAgent(sessions.getUserAgent()),sessions.getCreatedAt())).toList()
                ).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public ResponseDto deleteSession(UUID userSessionId){
        User user = userService.findWithUserSessionsByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
        userSessionService.deleteSessionById(user,userSessionId);
        return new ResponseDto(currentSessionDeletedMessage);
    }

    @Transactional
    public ResponseDto deleteAllSessionsExceptCurrentByUser(){
        User user = userService.findWithUserSessionsByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
        userSessionService.deleteAllSessionsExceptCurrentByUser(user);
        return new ResponseDto(allSessionsDeletedMessage);
    }

}
