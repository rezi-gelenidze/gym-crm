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

@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    // Wrap Trainee Service methods
    public Trainee createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        return traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
    }

    public Optional<Trainee> getTrainee(Long userId) {
        return traineeService.getTrainee(userId);
    }

    public boolean deleteTrainee(Long userId) {
        return traineeService.deleteTrainee(userId);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    // Wrap Trainer Service methods
    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        return trainerService.createTrainer(firstName, lastName, specialization);
    }

    public Optional<Trainer> getTrainer(Long userId) {
        return trainerService.getTrainer(userId);
    }

    public Trainer updateTrainer(Trainer Trainer) {
        return trainerService.updateTrainer(Trainer);
    }

    // Wrap Training Service methods
    public Training createTraining(Long traineeId, Long trainerId, String trainingName, TrainingType trainingTypeName, String trainingDate, Duration trainingDuration) {
        return trainingService.createTraining(traineeId, trainerId, trainingName, trainingTypeName, trainingDate, trainingDuration);
    }

    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        return trainingService.getTraining(traineeId, trainerId);
    }
}
