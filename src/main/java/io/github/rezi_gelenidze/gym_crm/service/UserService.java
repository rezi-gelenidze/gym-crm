package io.github.rezi_gelenidze.gym_crm.service;

import java.security.SecureRandom;

import io.github.rezi_gelenidze.gym_crm.dto.auth.CredentialsDto;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.exception.InvalidCredentialsException;
import io.github.rezi_gelenidze.gym_crm.exception.UserNotFoundException;
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
    public void authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidCredentialsException();
    }

    // DTO overloaded, for convenience
    public void authenticate(CredentialsDto credentials) {
        this.authenticate(credentials.getUsername(), credentials.getPassword());
    }

    public void assertIdentity(String clientUsername, String targetUsername) {
        if (!clientUsername.equals(targetUsername)) {
            throw new InvalidCredentialsException();
        }
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

    public String generateRawPassword() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
        return this.random.ints(10, 0, allowedChars.length())
                .mapToObj(allowedChars::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
