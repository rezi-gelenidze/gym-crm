package io.github.rezi_gelenidze.gym_crm.dao;


import io.github.rezi_gelenidze.gym_crm.dao.TraineeDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDaoTest {
    private TraineeDao traineeDao;

    @BeforeEach
    public void setUp() {
        // Manually injecting a ConcurrentHashMap to the TrainerDao
        this.traineeDao = new TraineeDao();
        this.traineeDao.setTraineeStorage(new ConcurrentHashMap<>());
    }

    // Helper method to create a Trainee object
    private Trainee createDummyTrainee() {
        return new Trainee(
                1L,
                "Alice",
                "Johnson",
                "Alice.Johnson",
                "password",
                "1995-04-20",
                "123 Main St"
        );
    }

    @Test
    public void testSaveTrainee() {
        Trainee trainee = this.createDummyTrainee();

        traineeDao.saveTrainee(trainee);
        Optional<Trainee> retrievedTrainee = traineeDao.getTrainee(1L);

        assertTrue(retrievedTrainee.isPresent());
        assertEquals(trainee, retrievedTrainee.get());
    }

    @Test
    public void getTrainee_WhenTraineeDoesNotExist_ReturnsEmptyOptional() {
        Optional<Trainee> retrievedTrainee = traineeDao.getTrainee(1L);

        assertTrue(retrievedTrainee.isEmpty());
    }

    @Test
    public void getTrainee_WhenTraineeExists_ReturnsTrainee() {
        Trainee trainee = this.createDummyTrainee();
        traineeDao.saveTrainee(trainee);

        Optional<Trainee> retrievedTrainee = traineeDao.getTrainee(1L);

        assertTrue(retrievedTrainee.isPresent());
        assertEquals(trainee, retrievedTrainee.get());

    }

    @Test
    public void testUpdateTrainee_WhenTraineeExists_DoNotThrowException() {
        Trainee trainee = this.createDummyTrainee();
        traineeDao.saveTrainee(trainee);

        trainee.setAddress("Wall St");

        assertDoesNotThrow(() -> traineeDao.updateTrainee(trainee));

        Optional<Trainee> retrievedTrainee = traineeDao.getTrainee(1L);
        assertTrue(retrievedTrainee.isPresent());

        assertEquals("Wall St", retrievedTrainee.get().getAddress());
    }

    @Test
    public void testUpdateTrainee_WhenTraineeDoesNotExist_ThrowsIllegalArgumentException() {
        Trainee trainee = this.createDummyTrainee();

        assertThrows(IllegalArgumentException.class, () -> traineeDao.updateTrainee(trainee));
    }

    @Test
    public void testDeleteTrainee_WheTraineeExists_ReturnsTrue() {
        Trainee trainee = this.createDummyTrainee();
        traineeDao.saveTrainee(trainee);

        assertTrue(traineeDao.deleteTrainee(1L));
    }

    @Test
    public void testDeleteTrainee_WhenTraineeDoesNotExist_ReturnsFalse() {
        assertFalse(traineeDao.deleteTrainee(1L));
    }
}
