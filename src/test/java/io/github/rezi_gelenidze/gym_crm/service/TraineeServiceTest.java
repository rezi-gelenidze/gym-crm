package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.TraineeDto;
import io.github.rezi_gelenidze.gym_crm.dto.TraineeUpdateDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.NoSuchElementException;
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
        TraineeDto traineeDto = new TraineeDto("John", "Doe", LocalDate.parse("2000-01-15"), "123 Main St");
        User user = new User("John", "Doe", "John.Doe", "randomPass123");
        Trainee trainee = new Trainee(user, LocalDate.parse("2000-01-15"), "123 Main St");

        when(userService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(userService.generatePassword()).thenReturn("randomPass123");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(traineeDto);

        assertNotNull(createdTrainee);
        assertEquals("John.Doe", createdTrainee.getUser().getUsername());
        assertEquals("randomPass123", createdTrainee.getUser().getPassword());
        assertEquals("123 Main St", createdTrainee.getAddress());

        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "Alice.Smith";
        User user = new User("Alice", "Smith", username, "securePass");
        Trainee trainee = new Trainee(user, LocalDate.parse("1995-05-20"), "456 Elm St");

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.getTraineeByUsername(username);

        assertTrue(foundTrainee.isPresent());
        assertEquals("Alice", foundTrainee.get().getUser().getFirstName());
        assertEquals("Smith", foundTrainee.get().getUser().getLastName());
        assertEquals("456 Elm St", foundTrainee.get().getAddress());

        verify(traineeRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetTraineeByUsername_NotFound() {
        String username = "unknown.user";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Trainee> foundTrainee = traineeService.getTraineeByUsername(username);

        assertFalse(foundTrainee.isPresent());
        verify(traineeRepository, times(1)).findByUsername(username);
    }

    @Test
    void testUpdateTraineeProfile() {
        String username = "Mark.Brown";
        User user = new User("Mark", "Brown", username, "newPass");
        Trainee trainee = new Trainee(user, LocalDate.parse("1998-03-10"), "789 Oak St");

        TraineeUpdateDto updateDto = new TraineeUpdateDto(LocalDate.parse("1998-03-11"), "Updated Address");

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTraineeProfile(updateDto, username);

        assertNotNull(updatedTrainee);
        assertEquals("Updated Address", updatedTrainee.getAddress());
        assertEquals(LocalDate.parse("1998-03-11"), updatedTrainee.getDateOfBirth());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void testUpdateTraineeProfile_NotFound() {
        String username = "nonexistent.user";
        TraineeUpdateDto updateDto = new TraineeUpdateDto(LocalDate.parse("1998-03-11"), "Updated Address");

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> traineeService.updateTraineeProfile(updateDto, username));

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
