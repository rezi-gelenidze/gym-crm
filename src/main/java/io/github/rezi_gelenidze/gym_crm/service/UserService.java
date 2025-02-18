package io.github.rezi_gelenidze.gym_crm.service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.security.SecureRandom;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserService {
    PasswordEncoder passwordEncoder;

    private final Map<Long, Trainer> trainerStorage;
    private final Map<Long, Trainee> traineeStorage;

    // Class level attributes for generation
    private final AtomicLong traineeIdCounter = new AtomicLong(1);
    private final AtomicLong trainerIdCounter = new AtomicLong(1);

    private final SecureRandom random = new SecureRandom();

    @Autowired
    public UserService(Map<Long, Trainer> trainerStorage, Map<Long, Trainee> traineeStorage, PasswordEncoder passwordEncoder) {
        this.trainerStorage = trainerStorage;
        this.traineeStorage = traineeStorage;
        this.passwordEncoder = passwordEncoder;
    }

    // utility methods
    protected long generateTraineeId() {
        return this.traineeIdCounter.getAndIncrement();
    }

    protected long generateTrainerId() {
        return this.trainerIdCounter.getAndIncrement();
    }

    protected boolean isUsernameTaken(String username) {
        // checks if username is taken in either Trainer or Trainee storage
        return this.traineeStorage.values().stream().anyMatch(t -> t.getUsername().equals(username)) || this.trainerStorage.values().stream().anyMatch(t -> t.getUsername().equals(username));
    }

    protected String generateUsername(String firstName, String lastName) {
        // generate username based on first and last name + number if username is taken
        String base = firstName + "." + lastName;
        String username = base;
        int counter = 1;

        while (isUsernameTaken(username)) {
            username = base + counter;
            counter++;
        }

        return username;
    }

    protected String generatePassword() {
        // characters allowed in password
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";

        // generate random password of length 10
        String rawPassword = this.random.ints(10, 0, allowedChars.length())
                .mapToObj(allowedChars::charAt) // Convert indices to characters
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();

        // encode password and return hashed password
        return passwordEncoder.encode(rawPassword);
    }
}
