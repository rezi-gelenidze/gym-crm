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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GymFacade {
    private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        logger.info("Attempting to register new trainee: {} {}", firstName, lastName);
        Trainee trainee = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
        logger.info("Trainee registered successfully: Username={}, ID={}", trainee.getUsername(), trainee.getUserId());
        return trainee;
    }

    public Optional<Trainee> getTrainee(Long userId) {
        logger.info("Fetching trainee with ID={}", userId);
        Optional<Trainee> trainee = traineeService.getTrainee(userId);
        if (trainee.isPresent()) {
            logger.info("Trainee found: Username={}", trainee.get().getUsername());
        } else {
            logger.warn("No trainee found with ID={}", userId);
        }
        return trainee;
    }

    public boolean deleteTrainee(Long userId) {
        logger.info("Attempting to delete trainee with ID={}", userId);
        boolean deleted = traineeService.deleteTrainee(userId);
        if (deleted) {
            logger.info("Trainee with ID={} successfully deleted", userId);
        } else {
            logger.warn("Failed to delete trainee with ID={}", userId);
        }
        return deleted;
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee: ID={}, Username={}", trainee.getUserId(), trainee.getUsername());
        Trainee updatedTrainee = traineeService.updateTrainee(trainee);
        logger.info("Trainee updated successfully: ID={}, New Address={}", updatedTrainee.getUserId(), updatedTrainee.getAddress());
        return updatedTrainee;
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        logger.info("Attempting to register new trainer: {} {}", firstName, lastName);
        Trainer trainer = trainerService.createTrainer(firstName, lastName, specialization);
        logger.info("Trainer registered successfully: Username={}, ID={}, Specialization={}",
                trainer.getUsername(), trainer.getUserId(), specialization);
        return trainer;
    }

    public Optional<Trainer> getTrainer(Long userId) {
        logger.info("Fetching trainer with ID={}", userId);
        Optional<Trainer> trainer = trainerService.getTrainer(userId);
        if (trainer.isPresent()) {
            logger.info("Trainer found: Username={}", trainer.get().getUsername());
        } else {
            logger.warn("No trainer found with ID={}", userId);
        }
        return trainer;
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer: ID={}, Username={}", trainer.getUserId(), trainer.getUsername());
        Trainer updatedTrainer = trainerService.updateTrainer(trainer);
        logger.info("Trainer updated successfully: ID={}, New Specialization={}",
                updatedTrainer.getUserId(), updatedTrainer.getSpecialization());
        return updatedTrainer;
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, TrainingType trainingTypeName, String trainingDate, Duration trainingDuration) {
        logger.info("Scheduling training: Name={}, Type={}, Duration={} mins, TraineeID={}, TrainerID={}",
                trainingName, trainingTypeName.getTrainingTypeName(), trainingDuration.toMinutes(), traineeId, trainerId);
        Training training = trainingService.createTraining(traineeId, trainerId, trainingName, trainingTypeName, trainingDate, trainingDuration);
        logger.info("Training successfully scheduled: Name={}, Date={}, Duration={} mins",
                training.getTrainingTypeName().getTrainingTypeName(), training.getTrainingDate(), training.getTrainingDuration().toMinutes());
        return training;
    }

    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        logger.info("Fetching training session: TraineeID={}, TrainerID={}", traineeId, trainerId);
        Optional<Training> training = trainingService.getTraining(traineeId, trainerId);
        if (training.isPresent()) {
            logger.info("Training session found: Type={}, Date={}, Duration={} mins",
                    training.get().getTrainingTypeName().getTrainingTypeName(), training.get().getTrainingDate(), training.get().getTrainingDuration().toMinutes());
        } else {
            logger.warn("No training session found for TraineeID={} and TrainerID={}", traineeId, trainerId);
        }
        return training;
    }
}
