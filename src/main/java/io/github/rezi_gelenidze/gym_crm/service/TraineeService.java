package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.TraineeDto;
import io.github.rezi_gelenidze.gym_crm.dto.TraineeUpdateDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.TraineeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeService {
    private final UserService userService;
    private final TraineeRepository traineeRepository;

    public Trainee createTrainee(TraineeDto traineeDto) {
        log.info("Creating new trainee: {} {}, Date of Birth={}, Address={}",
                traineeDto.getFirstName(), traineeDto.getLastName(), traineeDto.getDateOfBirth(), traineeDto.getAddress());

        User newUser = new User(
                traineeDto.getFirstName(),
                traineeDto.getLastName(),
                userService.generateUsername(traineeDto.getFirstName(), traineeDto.getLastName()),
                userService.generatePassword()
        );

        Trainee trainee = new Trainee(
                newUser,
                traineeDto.getDateOfBirth(),
                traineeDto.getAddress()
        );

        Trainee savedTrainee = traineeRepository.save(trainee);

        log.info("Trainee successfully created: Username={}, ID={}, Address={}",
                savedTrainee.getUser().getUsername(), savedTrainee.getUser().getUserId(), savedTrainee.getAddress());

        return savedTrainee;
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        log.info("Fetching trainee with Username={}", username);

        Optional<Trainee> trainee = traineeRepository.findByUsername(username);

        if (trainee.isPresent())
            log.info("Trainee found: ID={}", trainee.get().getTraineeId());
        else
            log.warn("No trainee found with Username={}", username);

        return trainee;
    }

    public Trainee updateTraineeProfile(TraineeUpdateDto traineeUpdateDto, String username) {
        log.info("Updating trainee profile: Username={}", username);

        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Trainee not found with Username: " + username));

        // Update only the profile related fields (we do not touch User entity)
        if (traineeUpdateDto.getAddress() != null)
            trainee.setAddress(traineeUpdateDto.getAddress());
        if (traineeUpdateDto.getDateOfBirth() != null)
            trainee.setDateOfBirth(traineeUpdateDto.getDateOfBirth());

        Trainee updatedTrainee = traineeRepository.save(trainee);
        log.info("Trainee updated successfully: Username={}, Address={}, Date of Birth={}",
                updatedTrainee.getUser().getUsername(), updatedTrainee.getAddress(), updatedTrainee.getDateOfBirth());

        return updatedTrainee;
    }

    public void deleteTrainee(String username) {
        log.info("Attempting to delete trainee with Username={}", username);

        traineeRepository.deleteByUsername(username);

        log.info("Trainee with Username={} successfully deleted", username);
    }
}
