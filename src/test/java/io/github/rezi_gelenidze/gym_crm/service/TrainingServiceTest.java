package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TrainingDao;
import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTraining() {
        Long traineeId = 1L;
        Long trainerId = 2L;
        String trainingName = "Strength Training";
        TrainingType trainingType = new TrainingType("Strength Training");
        String trainingDate = "2025-03-10";
        Duration trainingDuration = Duration.ofHours(1);

        Training training = new Training(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        when(trainingDao.saveTraining(any(Training.class))).thenReturn(training);

        Training createdTraining = trainingService.createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

        assertNotNull(createdTraining);
        assertEquals(trainingName, createdTraining.getTrainingName());
        assertEquals(trainingType, createdTraining.getTrainingTypeName());
        assertEquals(trainingDuration, createdTraining.getTrainingDuration());

        verify(trainingDao, times(1)).saveTraining(any(Training.class));
    }

    @Test
    void testGetTraining() {
        Long traineeId = 1L;
        Long trainerId = 2L;
        Training training = new Training(traineeId, trainerId, "Yoga", new TrainingType("Yoga"), "2025-04-15", Duration.ofMinutes(45));
        when(trainingDao.getTraining(traineeId, trainerId)).thenReturn(Optional.of(training));

        Optional<Training> foundTraining = trainingService.getTraining(traineeId, trainerId);

        assertTrue(foundTraining.isPresent());
        assertEquals("Yoga", foundTraining.get().getTrainingName());

        verify(trainingDao, times(1)).getTraining(traineeId, trainerId);
    }

    @Test
    void testGetTraining_NotFound() {
        Long traineeId = 1L;
        Long trainerId = 2L;
        when(trainingDao.getTraining(traineeId, trainerId)).thenReturn(Optional.empty());

        Optional<Training> foundTraining = trainingService.getTraining(traineeId, trainerId);

        assertFalse(foundTraining.isPresent());

        verify(trainingDao, times(1)).getTraining(traineeId, trainerId);
    }
}
