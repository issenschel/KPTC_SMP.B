package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.model.main.User;
import com.example.kptc_smp.model.main.UserSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, UUID> {

    Optional<UserSession> findByUserAndUserAgent(User user, String userAgent);

    Optional<UserSession> findByUserAndUserAgentAndIpAddress(User user, String userAgent, String ipAddress);

    List<UserSession> findAllByUser(User user);

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.user = :user AND s.id != :sessionId")
    void deleteAllByUserExceptSession(@Param("user") User user, @Param("sessionId") UUID sessionId);

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.expiresAt < :cutoffDate")
    void deleteExpiredSessions(Instant cutoffDate);

    void deleteAllByUser(User user);
}
