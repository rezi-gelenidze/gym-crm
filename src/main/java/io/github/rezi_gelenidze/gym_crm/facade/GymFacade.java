package io.github.rezi_gelenidze.gym_crm.facade;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.service.TraineeService;
import io.github.rezi_gelenidze.gym_crm.service.TrainerService;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;

import java.time.Duration;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    // Constructor injection only in facade (according to task requirements)
    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        log.info("Attempting to register new trainee: {} {}", firstName, lastName);
        Trainee trainee = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
        log.info("Trainee registered successfully: Username={}, ID={}", trainee.getUsername(), trainee.getUserId());
        return trainee;
    }

    public Optional<Trainee> getTrainee(Long userId) {
        log.info("Fetching trainee with ID={}", userId);
        Optional<Trainee> trainee = traineeService.getTrainee(userId);
        if (trainee.isPresent()) {
            log.info("Trainee found: Username={}", trainee.get().getUsername());
        } else {
            log.warn("No trainee found with ID={}", userId);
        }
        return trainee;
    }

    public boolean deleteTrainee(Long userId) {
        log.info("Attempting to delete trainee with ID={}", userId);
        boolean deleted = traineeService.deleteTrainee(userId);
        if (deleted) {
            log.info("Trainee with ID={} successfully deleted", userId);
        } else {
            log.warn("Failed to delete trainee with ID={}", userId);
        }
        return deleted;
    }

    public Trainee updateTrainee(Trainee trainee) {
        log.info("Updating trainee: ID={}, Username={}", trainee.getUserId(), trainee.getUsername());
        Trainee updatedTrainee = traineeService.updateTrainee(trainee);
        log.info("Trainee updated successfully: ID={}, New Address={}", updatedTrainee.getUserId(), updatedTrainee.getAddress());
        return updatedTrainee;
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        log.info("Attempting to register new trainer: {} {}", firstName, lastName);
        Trainer trainer = trainerService.createTrainer(firstName, lastName, specialization);
        log.info("Trainer registered successfully: Username={}, ID={}, Specialization={}",
                trainer.getUsername(), trainer.getUserId(), specialization);
        return trainer;
    }

    public Optional<Trainer> getTrainer(Long userId) {
        log.info("Fetching trainer with ID={}", userId);
        Optional<Trainer> trainer = trainerService.getTrainer(userId);
        if (trainer.isPresent()) {
            log.info("Trainer found: Username={}", trainer.get().getUsername());
        } else {
            log.warn("No trainer found with ID={}", userId);
        }
        return trainer;
    }

    public Trainer updateTrainer(Trainer trainer) {
        log.info("Updating trainer: ID={}, Username={}", trainer.getUserId(), trainer.getUsername());
        Trainer updatedTrainer = trainerService.updateTrainer(trainer);
        log.info("Trainer updated successfully: ID={}, New Specialization={}",
                updatedTrainer.getUserId(), updatedTrainer.getSpecialization());
        return updatedTrainer;
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, TrainingType trainingTypeName, String trainingDate, Duration trainingDuration) {
        log.info("Scheduling training: Name={}, Type={}, Duration={} mins, TraineeID={}, TrainerID={}",
                trainingName, trainingTypeName.getTrainingTypeName(), trainingDuration.toMinutes(), traineeId, trainerId);
        Training training = trainingService.createTraining(traineeId, trainerId, trainingName, trainingTypeName, trainingDate, trainingDuration);
        log.info("Training successfully scheduled: Name={}, Date={}, Duration={} mins",
                training.getTrainingTypeName().getTrainingTypeName(), training.getTrainingDate(), training.getTrainingDuration().toMinutes());
        return training;
    }

    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        log.info("Fetching training session: TraineeID={}, TrainerID={}", traineeId, trainerId);
        Optional<Training> training = trainingService.getTraining(traineeId, trainerId);
        if (training.isPresent()) {
            log.info("Training session found: Type={}, Date={}, Duration={} mins",
                    training.get().getTrainingTypeName().getTrainingTypeName(), training.get().getTrainingDate(), training.get().getTrainingDuration().toMinutes());
        } else {
            log.warn("No training session found for TraineeID={} and TrainerID={}", traineeId, trainerId);
        }
        return training;
    }
}
