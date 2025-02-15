package io.github.rezi_gelenidze.gym_crm.config;

import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.service.*;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

@Component
public class StorageSeed {
    private static final Logger logger = Logger.getLogger(StorageSeed.class.getName());

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Value("${seed.file.path}")
    private String seedFilePath;

    @Autowired
    public StorageSeed(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostConstruct
    public void seedDatabase() {
        logger.info("Seeding database from file: " + seedFilePath);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SeedData seedData = objectMapper.readValue(new File(seedFilePath), SeedData.class);

            // Load trainees
            for (Trainee trainee : seedData.getTrainees()) {
                Trainee createdTrainee = traineeService.createTrainee(
                        trainee.getFirstName(),
                        trainee.getLastName(),
                        trainee.getDateOfBirth(),
                        trainee.getAddress()
                );
                logger.info("Seeded Trainee: " + createdTrainee.getUser().getUsername());
            }

            // Load trainers
            for (Trainer trainer : seedData.getTrainers()) {
                Trainer createdTrainer = trainerService.createTrainer(
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getSpecialization()
                );
                logger.info("Seeded Trainer: " + createdTrainer.getUser().getUsername());
            }

            // Load trainings
            for (TrainingData training : seedData.getTrainings()) {
                TrainingType trainingType = new TrainingType(training.getTrainingType());
                Training createdTraining = trainingService.createTraining(
                        training.getTraineeId(),
                        training.getTrainerId(),
                        training.getTrainingName(),
                        trainingType,
                        training.getTrainingDate(),
                        Duration.ofMinutes(training.getTrainingDurationMinutes())
                );
                logger.info("Seeded Training: " + createdTraining.getTrainingTypeName().getTrainingTypeName());
            }

            logger.info("Seeding completed successfully!");
        } catch (IOException e) {
            logger.severe("Error reading seeding file: " + e.getMessage());
        }
    }

    // Internal class for JSON parsing
    @Getter
    private static class SeedData {
        private List<Trainee> trainees;
        private List<Trainer> trainers;
        private List<TrainingData> trainings;
    }

    @Getter
    private static class TrainingData {
        private Long traineeId;
        private Long trainerId;
        private String trainingName;
        private String trainingType;
        private String trainingDate;
        private int trainingDurationMinutes;
    }
}
