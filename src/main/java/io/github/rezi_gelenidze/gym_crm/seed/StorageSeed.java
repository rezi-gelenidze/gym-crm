package io.github.rezi_gelenidze.gym_crm.seed;

import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingTypeRepository;
import io.github.rezi_gelenidze.gym_crm.service.*;

import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageSeed {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeRepository trainingTypeRepository;

    @Value("${seed.file.path}")
    private String seedFilePath;

    @PostConstruct
    public void seedDatabase() throws IOException {
        log.info("Seeding database from file: {}", seedFilePath);

        // If rows already exist, skip seeding
        if (trainingTypeRepository.count() > 0) {
            log.info("Database already seeded, skipping...");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            SeedData seedData = objectMapper.readValue(new File(seedFilePath), SeedData.class);

            for (String trainingType : seedData.getTrainingTypes()) {
                TrainingType newTrainingType = new TrainingType(trainingType);
                trainingTypeRepository.save(newTrainingType);

                log.info("Seeded Training Type: Name={}, ID={}", newTrainingType.getTrainingTypeName(), newTrainingType.getTrainingTypeId());
            }

            // Load trainees
            for (TraineeCreateDto traineeCreateDto : seedData.getTrainees()) {
                Map<String, String> createdTrainee = traineeService.createTrainee(traineeCreateDto);
                log.info("Seeded Trainee: Username={}", createdTrainee.get("username"));
            }

            // Load trainers
            for (TrainerCreateDto trainerCreateDto : seedData.getTrainers()) {
                Map<String, String> createdTrainer = trainerService.createTrainer(trainerCreateDto);
                log.info("Seeded Trainer: Username={}", createdTrainer.get("username"));
            }

            // Load trainings
            for (TrainingCreateDto trainingCreateDto : seedData.getTrainings()) {
                Training createdTraining = trainingService.createTraining(trainingCreateDto);
                log.info("Seeded Training: Name={}, Type={}, TraineeID={}, TrainerID={}", createdTraining.getTrainingName(), createdTraining.getTrainingType().getTrainingTypeName(), createdTraining.getTrainee(), createdTraining.getTrainer());
            }

            log.info("Seeding completed successfully!");
        } catch (IOException e) {
            log.error("Error reading seeding file: {}", e.getMessage(), e);
        }
    }

    // Internal class for JSON parsing
    @Getter
    private static class SeedData {
        private List<TraineeCreateDto> trainees;
        private List<TrainerCreateDto> trainers;
        private List<TrainingCreateDto> trainings;
        private List<String> trainingTypes;
    }
}
