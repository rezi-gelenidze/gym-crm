package io.github.rezi_gelenidze.gym_crm.service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.security.SecureRandom;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private PasswordEncoder passwordEncoder;

    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;

    // Class level attributes for generation
    private final AtomicLong traineeIdCounter = new AtomicLong(1);
    private final AtomicLong trainerIdCounter = new AtomicLong(1);

    private final SecureRandom random = new SecureRandom();

    // Setter injections (according to task requirements)
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    // Utility methods
    public long generateTraineeId() {
        long id = this.traineeIdCounter.getAndIncrement();
        log.info("Generated new Trainee ID: {}", id);
        return id;
    }

    public long generateTrainerId() {
        long id = this.trainerIdCounter.getAndIncrement();
        log.info("Generated new Trainer ID: {}", id);
        return id;
    }

    public boolean isUsernameTaken(String username) {
        boolean taken = this.traineeStorage.values().stream().anyMatch(t -> t.getUsername().equals(username))
                || this.trainerStorage.values().stream().anyMatch(t -> t.getUsername().equals(username));

        log.debug("Checked if username '{}' is taken: {}", username, taken);
        return taken;
    }

    public String generateUsername(String firstName, String lastName) {
        String base = firstName + "." + lastName;
        String username = base;
        int counter = 1;

        while (isUsernameTaken(username)) {
            username = base + counter;
            counter++;
        }

        log.info("Generated unique username: {}", username);
        return username;
    }

    public String generatePassword() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";

        String rawPassword = this.random.ints(10, 0, allowedChars.length())
                .mapToObj(allowedChars::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();

        String hashedPassword = passwordEncoder.encode(rawPassword);
        log.info("Generated and encoded password for new user");

        return hashedPassword;
    }
}
