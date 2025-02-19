package io.github.rezi_gelenidze.gym_crm.facade;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.service.TraineeService;
import io.github.rezi_gelenidze.gym_crm.service.TrainerService;
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

class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "John.Doe", "securePass", "2000-02-15", "123 Main St");
        when(traineeService.createTrainee("John", "Doe", "2000-02-15", "123 Main St")).thenReturn(trainee);

        Trainee createdTrainee = gymFacade.createTrainee("John", "Doe", "2000-02-15", "123 Main St");

        assertNotNull(createdTrainee);
        assertEquals("John.Doe", createdTrainee.getUsername());

        verify(traineeService, times(1)).createTrainee("John", "Doe", "2000-02-15", "123 Main St");
    }

    @Test
    void testGetTrainee() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee(traineeId, "Alice", "Smith", "Alice.Smith", "randomPass", "1998-05-10", "456 Elm St");
        when(traineeService.getTrainee(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = gymFacade.getTrainee(traineeId);

        assertTrue(foundTrainee.isPresent());
        assertEquals("Alice.Smith", foundTrainee.get().getUsername());

        verify(traineeService, times(1)).getTrainee(traineeId);
    }

    @Test
    void testDeleteTrainee() {
        Long traineeId = 1L;
        when(traineeService.deleteTrainee(traineeId)).thenReturn(true);

        boolean result = gymFacade.deleteTrainee(traineeId);

        assertTrue(result);

        verify(traineeService, times(1)).deleteTrainee(traineeId);
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = new Trainee(1L, "Mark", "Brown", "Mark.Brown", "newPass", "1997-03-10", "789 Oak St");
        when(traineeService.updateTrainee(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = gymFacade.updateTrainee(trainee);

        assertNotNull(updatedTrainee);
        assertEquals("Mark.Brown", updatedTrainee.getUsername());

        verify(traineeService, times(1)).updateTrainee(trainee);
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer(1L, "Mike", "Johnson", "Mike.Johnson", "strongPass", "Strength Training");
        when(trainerService.createTrainer("Mike", "Johnson", "Strength Training")).thenReturn(trainer);

        Trainer createdTrainer = gymFacade.createTrainer("Mike", "Johnson", "Strength Training");

        assertNotNull(createdTrainer);
        assertEquals("Mike.Johnson", createdTrainer.getUsername());

        verify(trainerService, times(1)).createTrainer("Mike", "Johnson", "Strength Training");
    }

    @Test
    void testGetTrainer() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer(trainerId, "Sarah", "Parker", "Sarah.Parker", "yoga123", "Yoga");
        when(trainerService.getTrainer(trainerId)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = gymFacade.getTrainer(trainerId);

        assertTrue(foundTrainer.isPresent());
        assertEquals("Sarah.Parker", foundTrainer.get().getUsername());

        verify(trainerService, times(1)).getTrainer(trainerId);
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer(1L, "Paul", "Walker", "Paul.Walker", "crossfitPass", "CrossFit");
        when(trainerService.updateTrainer(trainer)).thenReturn(trainer);

        Trainer updatedTrainer = gymFacade.updateTrainer(trainer);

        assertNotNull(updatedTrainer);
        assertEquals("Paul.Walker", updatedTrainer.getUsername());

        verify(trainerService, times(1)).updateTrainer(trainer);
    }

    @Test
    void testCreateTraining() {
        Long traineeId = 1L;
        Long trainerId = 2L;
        String trainingName = "Weight Training";
        TrainingType trainingType = new TrainingType("Weight Training");
        String trainingDate = "2025-04-20";
        Duration trainingDuration = Duration.ofHours(1);

        Training training = new Training(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        when(trainingService.createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration))
                .thenReturn(training);

        Training createdTraining = gymFacade.createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

        assertNotNull(createdTraining);
        assertEquals("Weight Training", createdTraining.getTrainingName());

        verify(trainingService, times(1))
                .createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
    }

    @Test
    void testGetTraining() {
        Long traineeId = 1L;
        Long trainerId = 2L;
        Training training = new Training(traineeId, trainerId, "Cardio", new TrainingType("Cardio"), "2025-04-21", Duration.ofMinutes(45));
        when(trainingService.getTraining(traineeId, trainerId)).thenReturn(Optional.of(training));

        Optional<Training> foundTraining = gymFacade.getTraining(traineeId, trainerId);

        assertTrue(foundTraining.isPresent());
        assertEquals("Cardio", foundTraining.get().getTrainingName());

        verify(trainingService, times(1)).getTraining(traineeId, trainerId);
    }
}
