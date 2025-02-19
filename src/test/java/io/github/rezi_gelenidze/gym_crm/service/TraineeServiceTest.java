package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TraineeDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.service.TraineeService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        when(userService.generateTraineeId()).thenReturn(1L);
        when(userService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(userService.generatePassword()).thenReturn("randomPass123");

        Trainee trainee = new Trainee(1L, "John", "Doe", "John.Doe", "randomPass123", "2000-01-15", "123 Main St");
        when(traineeDao.saveTrainee(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee("John", "Doe", "2000-01-15", "123 Main St");

        assertNotNull(createdTrainee);
        assertEquals("John.Doe", createdTrainee.getUsername());
        assertEquals("randomPass123", createdTrainee.getPassword());
        assertEquals("123 Main St", createdTrainee.getAddress());

        verify(traineeDao, times(1)).saveTrainee(any(Trainee.class));
    }

    @Test
    void testGetTrainee() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee(traineeId, "Alice", "Smith", "Alice.Smith", "securePass", "1995-05-20", "456 Elm St");
        when(traineeDao.getTrainee(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.getTrainee(traineeId);

        assertTrue(foundTrainee.isPresent());
        assertEquals("Alice", foundTrainee.get().getFirstName());
        assertEquals("Smith", foundTrainee.get().getLastName());
        assertEquals("456 Elm St", foundTrainee.get().getAddress());

        verify(traineeDao, times(1)).getTrainee(traineeId);
    }

    @Test
    void testGetTrainee_NotFound() {
        Long traineeId = 99L;
        when(traineeDao.getTrainee(traineeId)).thenReturn(Optional.empty());

        Optional<Trainee> foundTrainee = traineeService.getTrainee(traineeId);

        assertFalse(foundTrainee.isPresent());

        verify(traineeDao, times(1)).getTrainee(traineeId);
    }

    @Test
    void testDeleteTrainee() {
        Long traineeId = 1L;
        when(traineeDao.deleteTrainee(traineeId)).thenReturn(true);

        boolean result = traineeService.deleteTrainee(traineeId);

        assertTrue(result);

        verify(traineeDao, times(1)).deleteTrainee(traineeId);
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = new Trainee(1L, "Mark", "Brown", "Mark.Brown", "newPass", "1998-03-10", "789 Oak St");
        when(traineeDao.updateTrainee(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        assertNotNull(updatedTrainee);
        assertEquals("Mark.Brown", updatedTrainee.getUsername());
        assertEquals("newPass", updatedTrainee.getPassword());
        assertEquals("789 Oak St", updatedTrainee.getAddress());

        verify(traineeDao, times(1)).updateTrainee(trainee);
    }
}
