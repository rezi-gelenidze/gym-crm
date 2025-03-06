package io.github.rezi_gelenidze.gym_crm.facade;

import io.github.rezi_gelenidze.gym_crm.dto.*;
import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainingService trainingService;

    public Trainer createTrainer(TrainerDto trainerDto) {
        return trainerService.createTrainer(trainerDto);
    }

    public Trainee createTrainee(TraineeDto traineeDto) {
        return traineeService.createTrainee(traineeDto);
    }

    public Optional<Trainer> getTrainerByUsername(CredentialsDto credentials, String queryUsername) {
        userService.authenticate(credentials);

        return trainerService.getTrainerByUsername(queryUsername);
    }

    public Optional<Trainee> getTraineeByUsername(CredentialsDto credentials, String queryUsername) {
        userService.authenticate(credentials);

        return traineeService.getTraineeByUsername(queryUsername);
    }

    public void updatePassword(CredentialsDto credentials, String newPassword) {
        userService.authenticate(credentials);

        userService.updatePassword(credentials.getUsername(), newPassword);
    }

    public Trainer updateTrainerProfile(CredentialsDto credentials, TrainerUpdateDto trainerUpdateDto) {
        userService.authenticate(credentials);

        return trainerService.updateTrainerProfile(trainerUpdateDto, credentials.getUsername());
    }

    public Trainee updateTraineeProfile(CredentialsDto credentials, TraineeUpdateDto traineeUpdateDto) {
        userService.authenticate(credentials);

        return traineeService.updateTraineeProfile(traineeUpdateDto, credentials.getUsername());
    }

    public void updateUserActiveStatus(CredentialsDto credentials, boolean active) {
        userService.authenticate(credentials);

        userService.updateActiveStatus(credentials.getUsername(), active);
    }

    public void deleteTrainee(CredentialsDto credentials) {
        userService.authenticate(credentials);

        traineeService.deleteTrainee(credentials.getUsername());
    }

    public List<Training> getTraineeTrainings(CredentialsDto credentials, String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        userService.authenticate(credentials);

        return trainingService.getTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> getTrainerTrainings(CredentialsDto credentials, String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        userService.authenticate(credentials);

        return trainingService.getTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }

    public Training addTraining(CredentialsDto credentials, TrainingDto trainingDto) {
        userService.authenticate(credentials);

        return trainingService.createTraining(trainingDto);
    }

    public List<Trainer> getUnassignedTrainers(CredentialsDto credentials, String traineeUsername) {
        userService.authenticate(credentials);

        return trainerService.getUnassignedTrainers(traineeUsername);
    }
}
