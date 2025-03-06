package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.CredentialsDto;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ShouldPass_WhenCredentialsAreCorrect() {
        CredentialsDto credentials = new CredentialsDto("john.doe", "password123");
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        assertDoesNotThrow(() -> userService.authenticate(credentials));
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        CredentialsDto credentials = new CredentialsDto("unknown.user", "password");

        when(userRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.authenticate(credentials));
        assertEquals("User not found: unknown.user", exception.getMessage());
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIsIncorrect() {
        CredentialsDto credentials = new CredentialsDto("john.doe", "wrongpassword");
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.authenticate(credentials));
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void updatePassword_ShouldEncodePasswordAndUpdateUser() {
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("oldPassword");

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.updatePassword("john.doe", "newPassword");

        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updatePassword_ShouldDoNothing_WhenUserNotFound() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.empty());

        userService.updatePassword("john.doe", "newPassword");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateActiveStatus_ShouldUpdateUserStatus() {
        User user = new User();
        user.setUsername("john.doe");
        user.setActive(false);

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(user));

        userService.updateActiveStatus("john.doe", true);

        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateActiveStatus_ShouldDoNothing_WhenUserNotFound() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.empty());

        userService.updateActiveStatus("john.doe", true);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void generateUsername_ShouldReturnAvailableUsername() {
        when(userRepository.existsByUsername("John.Doe")).thenReturn(false);

        String username = userService.generateUsername("John", "Doe");

        assertEquals("John.Doe", username);
    }

    @Test
    void generateUsername_ShouldReturnUniqueUsername_WhenNameIsTaken() {
        when(userRepository.existsByUsername("John.Doe")).thenReturn(true);
        when(userRepository.existsByUsername("John.Doe1")).thenReturn(false);

        String username = userService.generateUsername("John", "Doe");

        assertEquals("John.Doe1", username);
    }

    @Test
    void generatePassword_ShouldReturnEncodedPassword() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String password = userService.generatePassword();

        assertEquals("encodedPassword", password);
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}
