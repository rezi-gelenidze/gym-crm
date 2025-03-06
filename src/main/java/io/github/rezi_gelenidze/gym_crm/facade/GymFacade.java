package io.github.rezi_gelenidze.gym_crm.facade;

import io.github.rezi_gelenidze.gym_crm.dto.*;
import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainingService trainingService;

    public Trainer createTrainer(@Valid TrainerDto trainerDto) {
        return trainerService.createTrainer(trainerDto);
    }

    public Trainee createTrainee(@Valid TraineeDto traineeDto) {
        return traineeService.createTrainee(traineeDto);
    }

    public Optional<Trainer> getTrainerByUsername(@Valid CredentialsDto credentials, String queryUsername) {
        userService.authenticate(credentials);

        return trainerService.getTrainerByUsername(queryUsername);
    }

    public Optional<Trainee> getTraineeByUsername(@Valid CredentialsDto credentials, String queryUsername) {
        userService.authenticate(credentials);

        return traineeService.getTraineeByUsername(queryUsername);
    }

    public void updatePassword(@Valid CredentialsDto credentials, @Valid String newPassword) {
        userService.authenticate(credentials);

        userService.updatePassword(credentials.getUsername(), newPassword);
    }

    public Trainer updateTrainerProfile(@Valid CredentialsDto credentials, @Valid TrainerUpdateDto trainerUpdateDto) {
        userService.authenticate(credentials);

        return trainerService.updateTrainerProfile(trainerUpdateDto, credentials.getUsername());
    }

    public Trainee updateTraineeProfile(@Valid CredentialsDto credentials, @Valid TraineeUpdateDto traineeUpdateDto) {
        userService.authenticate(credentials);

        return traineeService.updateTraineeProfile(traineeUpdateDto, credentials.getUsername());
    }

    public void updateUserActiveStatus(@Valid CredentialsDto credentials, boolean active) {
        userService.authenticate(credentials);

        userService.updateActiveStatus(credentials.getUsername(), active);
    }

    public void deleteTrainee(@Valid CredentialsDto credentials) {
        userService.authenticate(credentials);

        traineeService.deleteTrainee(credentials.getUsername());
    }

    public List<Training> getTraineeTrainings(@Valid CredentialsDto credentials, String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        userService.authenticate(credentials);

        return trainingService.getTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> getTrainerTrainings(@Valid CredentialsDto credentials, String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        userService.authenticate(credentials);

        return trainingService.getTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }

    public Training addTraining(@Valid CredentialsDto credentials, @Valid TrainingDto trainingDto) {
        userService.authenticate(credentials);

        return trainingService.createTraining(trainingDto);
    }

    public List<Trainer> getUnassignedTrainers(@Valid CredentialsDto credentials, String traineeUsername) {
        userService.authenticate(credentials);

        return trainerService.getUnassignedTrainers(traineeUsername);
    }
}
