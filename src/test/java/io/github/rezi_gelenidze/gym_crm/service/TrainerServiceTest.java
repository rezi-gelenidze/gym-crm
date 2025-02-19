package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TrainerDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.service.TrainerService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainer() {
        when(userService.generateTrainerId()).thenReturn(1L);
        when(userService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(userService.generatePassword()).thenReturn("securePass123");

        Trainer trainer = new Trainer(1L, "John", "Doe", "John.Doe", "securePass123", "Fitness");
        when(trainerDao.saveTrainer(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer("John", "Doe", "Fitness");

        assertNotNull(createdTrainer);
        assertEquals("John.Doe", createdTrainer.getUsername());
        assertEquals("Fitness", createdTrainer.getSpecialization());

        verify(trainerDao, times(1)).saveTrainer(any(Trainer.class));
    }

    @Test
    void testGetTrainer() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer(trainerId, "Alice", "Smith", "Alice.Smith", "randomPass", "Yoga");
        when(trainerDao.getTrainer(trainerId)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.getTrainer(trainerId);

        assertTrue(foundTrainer.isPresent());
        assertEquals("Alice", foundTrainer.get().getFirstName());
        assertEquals("Yoga", foundTrainer.get().getSpecialization());

        verify(trainerDao, times(1)).getTrainer(trainerId);
    }

    @Test
    void testGetTrainer_NotFound() {
        Long trainerId = 99L;
        when(trainerDao.getTrainer(trainerId)).thenReturn(Optional.empty());

        Optional<Trainer> foundTrainer = trainerService.getTrainer(trainerId);

        assertFalse(foundTrainer.isPresent());

        verify(trainerDao, times(1)).getTrainer(trainerId);
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer(1L, "Mark", "Brown", "Mark.Brown", "newPass123", "CrossFit");
        when(trainerDao.updateTrainer(trainer)).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        assertNotNull(updatedTrainer);
        assertEquals("Mark.Brown", updatedTrainer.getUsername());
        assertEquals("CrossFit", updatedTrainer.getSpecialization());

        verify(trainerDao, times(1)).updateTrainer(trainer);
    }
}
