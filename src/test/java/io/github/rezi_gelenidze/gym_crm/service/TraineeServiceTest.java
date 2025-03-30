package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeProfileDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        TraineeCreateDto traineeCreateDto = new TraineeCreateDto("John", "Doe", LocalDate.parse("2000-01-15"), "123 Main St");
        User user = new User("John", "Doe", "John.Doe", "randomPass123");
        Trainee trainee = new Trainee(user, LocalDate.parse("2000-01-15"), "123 Main St");

        when(userService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(userService.generateRawPassword()).thenReturn("randomPass123");
        when(userService.hashPassword("randomPass123")).thenReturn("randomHash123");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Map<String, String> credentials = traineeService.createTrainee(traineeCreateDto);

        assertNotNull(credentials);
        assertEquals("John.Doe", credentials.get("username"));
        assertEquals("randomPass123", credentials.get("password"));

        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "Alice.Smith";
        User user = new User("Alice", "Smith", username, "securePass");
        Trainee trainee = new Trainee(user, LocalDate.parse("1995-05-20"), "456 Elm St");

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        Optional<TraineeProfileDto> foundTrainee = traineeService.getTraineeProfile(username);

        assertTrue(foundTrainee.isPresent());
        assertEquals("Alice", foundTrainee.get().getFirstName());
        assertEquals("Smith", foundTrainee.get().getLastName());
        assertEquals("456 Elm St", foundTrainee.get().getAddress());

        verify(traineeRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetTraineeByUsername_NotFound() {
        String username = "unknown.user";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<TraineeProfileDto> foundTrainee = traineeService.getTraineeProfile(username);

        assertFalse(foundTrainee.isPresent());
        verify(traineeRepository, times(1)).findByUsername(username);
    }

    @Test
    void testDeleteTrainee() {
        String username = "John.Doe";

        doNothing().when(traineeRepository).deleteByUsername(username);

        traineeService.deleteTrainee(username);

        verify(traineeRepository, times(1)).deleteByUsername(username);
    }
}
