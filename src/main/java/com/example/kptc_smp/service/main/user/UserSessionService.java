package com.example.kptc_smp.service.main.user;

import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserSession;
import com.example.kptc_smp.repository.main.UserSessionRepository;
import com.example.kptc_smp.utility.JwtTokenUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final HttpServletRequest request;

    @Transactional
    public UserSession createSession(User user) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        userSessionRepository.findByUserAndUserAgentAndIpAddress(user, userAgent,ipAddress).ifPresent(userSessionRepository::delete);

        return userSessionRepository.save(createNewSession(user,userAgent,ipAddress));
    }

    public UserSession createNewSession(User user, String userAgent, String ipAddress) {
        UserSession userSession = new UserSession();

        userSession.setUser(user);
        userSession.setRefreshToken(jwtTokenUtils.generateRefreshToken());
        userSession.setUserAgent(userAgent);
        userSession.setIpAddress(ipAddress);
        userSession.setCreatedAt(Instant.now());
        userSession.setExpiresAt(jwtTokenUtils.getRefreshTokenExpiration());

        return userSession;
    }



    @Transactional
    public void deleteAllSessionsByUser(User user) {
        userSessionRepository.deleteAllByUser(user);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupExpiredSessions() {
        userSessionRepository.deleteExpiredSessions(Instant.now());
    }
}