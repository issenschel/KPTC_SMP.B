package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.model.main.UserSession;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import com.example.kptc_smp.repository.main.UserSessionRepository;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

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

        userSessionRepository.findByUserAndUserAgentAndIpAddress(user, userAgent, ipAddress).ifPresent(userSessionRepository::delete);

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


    public List<UserSession> getAllSessionsByUser(User user){
       return userSessionRepository.findAllByUser(user);
    }

    public void deleteAllSessionsExceptCurrentByUser(User user) {
        String userAgent = clientInfoService.getClientUserAgent();
        String ipAddress = clientInfoService.getClientIp();

        user.getUserSessions().stream()
                .filter(userSession -> userSession.getUserAgent().equals(userAgent) && userSession.getIpAddress().equals(ipAddress)).findFirst()
                .ifPresentOrElse(
                        userSession -> userSessionRepository.deleteAllByUserExceptSession(user, userSession.getId()),
                        () -> userSessionRepository.deleteAllByUser(user)
                );
    }

    public void deleteSessionById(User user, int userSessionId) {
        user.getUserSessions().stream().filter(userSession -> userSession.getId() == userSessionId).findFirst()
                .ifPresentOrElse(
                        userSession -> userSessionRepository.deleteById((long) userSessionId),
                        () -> { throw new UserNotFoundException();});
    }

    @Transactional
    public void deleteAllSessionsByUser(User user) {
        userSessionRepository.deleteAllByUser(user);
    }

    @Scheduled(cron = "${scheduled.session.cleanup.cron}")
    @Transactional
    public void cleanupExpiredSessions() {
        userSessionRepository.deleteExpiredSessions(Instant.now());
    }
}