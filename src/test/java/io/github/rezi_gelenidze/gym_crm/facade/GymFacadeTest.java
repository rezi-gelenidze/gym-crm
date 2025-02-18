package io.github.rezi_gelenidze.gym_crm.facade;

import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.config.AppConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GymFacadeTest {
    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        // Manually initialize the Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        gymFacade = context.getBean(GymFacade.class);
    }

    @Test
    void testCRUDTrainee() {
        // Create a Trainee
        Trainee trainee = gymFacade.createTrainee("Alice", "Johnson", "1995-04-20", "123 Main St");

        assertNotNull(trainee);
        assertNotNull(trainee.getUserId());
        assertEquals("Alice.Johnson", trainee.getUsername());

        // Get the Trainee and verify it was created
        Optional<Trainee> retrievedTrainee = gymFacade.getTrainee(trainee.getUserId());
        assertTrue(retrievedTrainee.isPresent());

        // Update the Trainee
        trainee.setAddress("Wall St");
        Trainee updatedTrainee = gymFacade.updateTrainee(trainee);

        assertEquals("Wall St", updatedTrainee.getAddress());

        // Delete the Trainee
        assertTrue(gymFacade.deleteTrainee(trainee.getUserId()));

        // Verify the Trainee was deleted
        assertTrue(gymFacade.getTrainee(trainee.getUserId()).isEmpty());
    }

    @Test
    void testCRUTrainer() {
        // Create a Trainer
        Trainer trainer = gymFacade.createTrainer("John", "Doe", "Strength Training");

        assertNotNull(trainer);
        assertNotNull(trainer.getUserId());
        assertEquals("John.Doe", trainer.getUsername());

        // Get the Trainer and verify it was created
        Optional<Trainer> retrievedTrainer = gymFacade.getTrainer(trainer.getUserId());
        assertTrue(retrievedTrainer.isPresent());

        // Update the Trainer
        trainer.setSpecialization("Cardio Training");
        Trainer updatedTrainer = gymFacade.updateTrainer(trainer);

        assertEquals("Cardio Training", updatedTrainer.getSpecialization());
    }

    @Test
    void testCRTraining() {
        Trainee trainee = gymFacade.createTrainee("Alice", "Johnson", "1995-04-20", "123 Main St");
        Trainer trainer = gymFacade.createTrainer("John", "Doe", "Strength Training");

        TrainingType trainingType = new TrainingType("Strength Training");

        Training training = gymFacade.createTraining(
                trainee.getUserId(),
                trainer.getUserId(),
                "Strength Training",
                trainingType,
                "2025-02-15",
                Duration.ofMinutes(60)
        );

        assertNotNull(training);
        assertEquals("Strength Training", training.getTrainingTypeName().getTrainingTypeName());

        // Get the training and verify it was created
        Optional<Training> retrievedTraining = gymFacade.getTraining(training.getTraineeId(), training.getTrainerId());
        assertTrue(retrievedTraining.isPresent());
    }

    @Test
    void testUserFieldsGeneration() {
        // Create 3 users 2 with the same first and last name
        Trainee trainee1 = gymFacade.createTrainee("Alice", "Johnson", "1995-04-20", "123 Main St");
        Trainee trainee2 = gymFacade.createTrainee("Alice", "Johnson", "1997-12-11", "125 Main St");
        Trainer trainer1 = gymFacade.createTrainer("Bob", "Smith", "Strength Training");

        assertNotNull(trainee1);
        assertNotNull(trainee2);
        assertNotNull(trainer1);

        // Verify that the usernames are unique
        assertNotEquals(trainee1.getUsername(), trainee2.getUsername());
        assertNotEquals(trainee2.getUsername(), trainer1.getUsername());

        // Verify incremental userId generation
        assertEquals(trainee1.getUserId() + 1, trainee2.getUserId());

        // Verify same username clash handling
        assertEquals("Alice.Johnson", trainee1.getUsername());
        assertEquals("Alice.Johnson1", trainee2.getUsername());

        // Verify password generation randomization
        assertNotEquals(trainee1.getPassword(), trainee2.getPassword());
        assertNotEquals(trainee2.getPassword(), trainer1.getPassword());
    }
}
