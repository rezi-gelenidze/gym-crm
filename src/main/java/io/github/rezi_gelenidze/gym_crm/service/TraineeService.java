package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeProfileDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerListItemDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.TraineeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeService {
    private final UserService userService;
    private final TraineeRepository traineeRepository;

    public Map<String, String> createTrainee(TraineeCreateDto traineeCreateDto) {
        log.info("Creating new trainee: {} {}, Date of Birth={}, Address={}",
                traineeCreateDto.getFirstName(), traineeCreateDto.getLastName(), traineeCreateDto.getDateOfBirth(), traineeCreateDto.getAddress());

        // preserve raw password to return to user (as requested in requirements)
        String rawPassword = userService.generateRawPassword();

        User newUser = new User(
                traineeCreateDto.getFirstName(),
                traineeCreateDto.getLastName(),
                userService.generateUsername(traineeCreateDto.getFirstName(), traineeCreateDto.getLastName()),
                userService.hashPassword(rawPassword)
        );

        Trainee trainee = new Trainee(
                newUser,
                traineeCreateDto.getDateOfBirth(),
                traineeCreateDto.getAddress()
        );

        Trainee savedTrainee = traineeRepository.save(trainee);

        log.info("Trainee successfully created: Username={}, ID={}, Address={}",
                savedTrainee.getUser().getUsername(), savedTrainee.getUser().getUserId(), savedTrainee.getAddress());

        return Map.of(
                "username", savedTrainee.getUser().getUsername(),
                "password", rawPassword
        );
    }

    public Optional<TraineeProfileDto> getTraineeProfile(String username) {
        log.info("Fetching trainee with Username={}", username);

        // query the trainer itself
        Trainee trainee = traineeRepository.findByUsername(username).orElse(null);

        if (trainee == null) return Optional.empty();

        // query associated trainees
        List<TrainerListItemDto> trainers = traineeRepository.findTraineeTrainers(username);


        TraineeProfileDto traineeProfileDto = new TraineeProfileDto(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                trainers
        );

        return Optional.of(traineeProfileDto);
    }

    public void deleteTrainee(String username) {
        log.info("Attempting to delete trainee with Username={}", username);

        traineeRepository.deleteByUsername(username);

        log.info("Trainee with Username={} successfully deleted", username);
    }
}
