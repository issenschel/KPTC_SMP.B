package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.entity.main.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByRefreshToken(String refreshToken);

    Optional<UserSession> findByUserAndUserAgentAndIpAddress(User user, String userAgent, String ipAddress);

    List<UserSession> findAllByUser(User user);

    @Query("DELETE FROM UserSession s WHERE s.expiresAt < :cutoffDate")
    void deleteExpiredSessions(Instant cutoffDate);


    void deleteAllByUser(User user);
}
