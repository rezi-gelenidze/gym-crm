package io.github.rezi_gelenidze.gym_crm.seed;

import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.service.*;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class StorageSeed {
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
        log.info("Seeding database from file: {}", seedFilePath);

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
                log.info("Seeded Trainee: Username={}, ID={}", createdTrainee.getUsername(), createdTrainee.getUserId());
            }

            // Load trainers
            for (Trainer trainer : seedData.getTrainers()) {
                Trainer createdTrainer = trainerService.createTrainer(
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getSpecialization()
                );
                log.info("Seeded Trainer: Username={}, ID={}", createdTrainer.getUsername(), createdTrainer.getUserId());
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
                log.info("Seeded Training: Name={}, Type={}, TraineeID={}, TrainerID={}",
                        createdTraining.getTrainingName(), createdTraining.getTrainingTypeName().getTrainingTypeName(),
                        createdTraining.getTraineeId(), createdTraining.getTrainerId());
            }

            log.info("Seeding completed successfully!");
        } catch (IOException e) {
            log.error("Error reading seeding file: {}", e.getMessage(), e);
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
