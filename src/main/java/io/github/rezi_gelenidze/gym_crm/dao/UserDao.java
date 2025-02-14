package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.security.SecureRandom;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.storage.MemoryStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    private final Map<Long, Trainer> trainerStorage;
    private final Map<Long, Trainee> traineeStorage;

    // Class level attributes for generation
    private final AtomicLong userIdGenerator = new AtomicLong(1);
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public UserDao(MemoryStorage memoryStorage) {
        this.trainerStorage = memoryStorage.getTrainerStorage();
        this.traineeStorage = memoryStorage.getTraineeStorage();
    }

    public User createUser(String firstName, String lastName) {
        // generate userId, username, and password
        Long userId = generateUserId();
        String username = generateUsername(firstName, lastName);
        String password = generatePassword();

        // create and return User object which will be injected as Trainer or Trainee
        return new User(userId, firstName, lastName, username, password, true);
    }

    // utility methods
    private Long generateUserId() {
        // increment userId by 1 for each new user
        return this.userIdGenerator.getAndIncrement();
    }

    private boolean isUsernameTaken(String username) {
        // checks if username is taken in either Trainer or Trainee storage
        return this.traineeStorage.values().stream().anyMatch(t -> t.getUser().getUsername().equals(username)) ||
                this.trainerStorage.values().stream().anyMatch(t -> t.getUser().getUsername().equals(username));
    }

    private String generateUsername(String firstName, String lastName) {
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

    private String generatePassword() {
        // generate random password of length 10 with alphanumeric and special characters
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";

        return this.random.ints(10, 0, allowedChars.length()).mapToObj(allowedChars::charAt) // Convert indices to characters
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }
}
