package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        this.trainerStorage = new ConcurrentHashMap<>();
        this.traineeStorage = new ConcurrentHashMap<>();

        userService = new UserService();
        userService.setTrainerStorage(trainerStorage);
        userService.setTraineeStorage(traineeStorage);
        userService.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void generateTraineeId_ShouldReturnIncrementalIds() {
        long id1 = userService.generateTraineeId();
        long id2 = userService.generateTraineeId();
        assertEquals(1L, id1);
        assertEquals(2L, id2);
    }

    @Test
    void generateTrainerId_ShouldReturnIncrementalIds() {
        long id1 = userService.generateTrainerId();
        long id2 = userService.generateTrainerId();
        assertEquals(1L, id1);
        assertEquals(2L, id2);
    }

    @Test
    void isUsernameTaken_WhenUsernameExistsInTrainees_ShouldReturnTrue() {
        Trainee trainee = new Trainee();
        trainee.setUsername("Alice.Johnson");
        traineeStorage.put(1L, trainee);

        assertTrue(userService.isUsernameTaken("Alice.Johnson"));
    }

    @Test
    void isUsernameTaken_WhenUsernameExistsInTrainers_ShouldReturnTrue() {
        Trainer trainer = new Trainer();
        trainer.setUsername("John.Doe");
        trainerStorage.put(1L, trainer);

        assertTrue(userService.isUsernameTaken("John.Doe"));
    }

    @Test
    void isUsernameTaken_WhenUsernameDoesNotExist_ShouldReturnFalse() {
        assertFalse(userService.isUsernameTaken("New.User"));
    }

    @Test
    void generateUsername_WhenUsernameIsAvailable_ShouldReturnUsername() {
        String username = userService.generateUsername("Alice", "Johnson");
        assertEquals("Alice.Johnson", username);
    }

    @Test
    void generateUsername_WhenUsernameIsTaken_ShouldReturnUniqueUsername() {
        Trainee trainee = new Trainee();
        trainee.setUsername("Alice.Johnson");
        traineeStorage.put(1L, trainee);

        String username = userService.generateUsername("Alice", "Johnson");
        assertEquals("Alice.Johnson1", username);
    }

    @Test
    void generatePassword_ShouldReturnEncodedPassword() {
        // Mocking the passwordEncoder.encode method
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String password = userService.generatePassword();
        assertEquals("encodedPassword", password);

        // Verifying that passwordEncoder.encode was called once
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}
