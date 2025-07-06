package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.exception.user.ActiveSessionDeletionException;
import com.example.kptc_smp.exception.user.UserSessionNotFound;
import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.model.main.UserSession;
import com.example.kptc_smp.repository.main.UserSessionRepository;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final ClientInfoService clientInfoService;

    @Transactional
    public UserSession createSession(User user) {
        String userAgent = clientInfoService.getClientUserAgent();
        String ipAddress = clientInfoService.getClientIp();

        userSessionRepository.findByUserAndUserAgent(user, userAgent).ifPresent(userSessionRepository::delete);

        return userSessionRepository.save(createNewSession(user, userAgent, ipAddress));
    }

    public UserSession createNewSession(User user, String userAgent, String ipAddress) {
        UserSession userSession = new UserSession();

        userSession.setUser(user);
        userSession.setRefreshToken(jwtTokenUtils.generateRefreshToken(user.getUsername()));
        userSession.setUserAgent(userAgent);
        userSession.setIpAddress(ipAddress);
        userSession.setCreatedAt(Instant.now());
        userSession.setExpiresAt(jwtTokenUtils.getRefreshTokenExpiration());

        return userSession;
    }


    public List<UserSession> getAllSessionsByUser(User user) {
        return userSessionRepository.findAllByUser(user);
    }

    public void deleteAllSessionsExceptCurrentByUser(User user) {
        UUID currentSessionId = (UUID) SecurityContextHolder.getContext().getAuthentication().getDetails();

        userSessionRepository.deleteAllByUserExceptSession(user, currentSessionId);
    }

    public void deleteSessionById(User user, UUID userSessionId) {
        UUID currentSessionId = (UUID) SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (userSessionId.equals(currentSessionId)) {
            throw new ActiveSessionDeletionException();
        }

        user.getUserSessions().stream().filter(userSession -> userSession.getId().equals(userSessionId)).findFirst()
                .ifPresentOrElse(
                        userSession -> user.getUserSessions().remove(userSession),
                        () -> {
                            throw new UserSessionNotFound();
                        });
    }

    public void deleteAllSessionsByUser(User user) {
        userSessionRepository.deleteAllByUser(user);
    }

    public Optional<UserSession> findByUUID(UUID id) {
        return userSessionRepository.findById(id);
    }

    @Scheduled(cron = "${scheduled.session.cleanup.cron}")
    @Transactional
    public void cleanupExpiredSessions() {
        userSessionRepository.deleteExpiredSessions(Instant.now());
    }
}