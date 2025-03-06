package io.github.rezi_gelenidze.gym_crm.service;

import java.security.SecureRandom;

import io.github.rezi_gelenidze.gym_crm.dto.CredentialsDto;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final SecureRandom random = new SecureRandom();

    // Authentication assertion, raises exception if authentication fails
    public void authenticate(CredentialsDto credentials) {
        User user = userRepository.findByUsername(credentials.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + credentials.getUsername()));

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid password");
    }

    public void updatePassword(String username, String newPassword) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        });
    }

    public void updateActiveStatus(String username, boolean active) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setActive(active);
            userRepository.save(user);
        });
    }

    public String generateUsername(String firstName, String lastName) {
        String base = firstName + "." + lastName;
        String username = base;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
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
