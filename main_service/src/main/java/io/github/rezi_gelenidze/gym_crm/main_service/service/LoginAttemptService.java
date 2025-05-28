package io.github.rezi_gelenidze.gym_crm.main_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    @Value("${app.auth.max-attempts}")
    private int MAX_ATTEMPTS;

    @Value("${app.auth.block-duration-ms}")
    private long BLOCK_DURATION_MS;

    // Concurrent hashmap (in enterprise redis can be used for more load :) )
    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    // Record failure
    public void loginFailed(String username) {
        LoginAttempt attempt = attempts.getOrDefault(username, new LoginAttempt(0, null));
        attempt.count++;
        if (attempt.count >= MAX_ATTEMPTS) {
            attempt.blockedUntil = Instant.now().plusMillis(BLOCK_DURATION_MS);
        }
        attempts.put(username, attempt);
    }

    // Record succession (by resetting failures)
    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    // Check status of block by failures
    public boolean isBlocked(String username) {
        LoginAttempt attempt = attempts.get(username);
        if (attempt == null) return false;
        if (attempt.blockedUntil == null) return false;
        if (Instant.now().isAfter(attempt.blockedUntil)) {
            attempts.remove(username);
            return false;
        }
        return true;
    }

    private static class LoginAttempt {
        int count;
        Instant blockedUntil;

        public LoginAttempt(int count, Instant blockedUntil) {
            this.count = count;
            this.blockedUntil = blockedUntil;
        }
    }
}
