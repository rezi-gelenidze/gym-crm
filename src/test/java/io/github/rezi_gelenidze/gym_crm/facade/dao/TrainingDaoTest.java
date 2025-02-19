package io.github.rezi_gelenidze.gym_crm.facade.dao;

import io.github.rezi_gelenidze.gym_crm.dao.TrainingDao;
import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDaoTest {
    private TrainingDao trainingDao;

    @BeforeEach
    public void setUp() {
        // Manually injecting a ConcurrentHashMap to the TrainingDao
        this.trainingDao = new TrainingDao();
        this.trainingDao.setTrainingStorage(new ConcurrentHashMap<>());
    }

    // Helper method to create a Training object
    private Training createDummyTraining() {
        return new Training(
                1L,
                2L,
                "Cardio",
                new TrainingType("Cardio"),
                "2021-08-01",
                Duration.ofMinutes(30)
        );
    }

    @Test
    public void testSaveTraining() {
        Training training = this.createDummyTraining();
        trainingDao.saveTraining(training);

        Optional<Training> retrievedTraining = trainingDao.getTraining(1L, 2L);

        assertTrue(retrievedTraining.isPresent());
        assertEquals(training, retrievedTraining.get());
    }

    @Test
    public void getTraining_WhenTrainingDoesNotExist_ReturnsEmptyOptional() {
        Optional<Training> retrievedTraining = trainingDao.getTraining(1L, 2L);

        assertTrue(retrievedTraining.isEmpty());
    }

    @Test
    public void getTraining_WhenTrainingExists_ReturnsTraining() {
        Training training = this.createDummyTraining();
        trainingDao.saveTraining(training);

        Optional<Training> retrievedTraining = trainingDao.getTraining(1L, 2L);

        assertTrue(retrievedTraining.isPresent());
        assertEquals(training, retrievedTraining.get());
    }
}
