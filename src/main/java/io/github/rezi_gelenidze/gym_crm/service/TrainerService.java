package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.TrainerDto;
import io.github.rezi_gelenidze.gym_crm.dto.TrainerUpdateDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService {
    private final UserService userService;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public Trainer createTrainer(TrainerDto trainerDto) {
        log.info("Creating new trainer: {} {}, Specialization={}",
                trainerDto.getFirstName(), trainerDto.getLastName(), trainerDto.getSpecialization());

        User newUser = new User(
                trainerDto.getFirstName(),
                trainerDto.getLastName(),
                userService.generateUsername(trainerDto.getFirstName(), trainerDto.getLastName()),
                userService.generatePassword()
        );

        TrainingType specialization = trainingTypeRepository.findByTrainingTypeName(trainerDto.getSpecialization())
                .orElseThrow(() -> new NoSuchElementException("Specialization not found: " + trainerDto.getSpecialization()));

        Trainer trainer = new Trainer(newUser, specialization);

        Trainer savedTrainer = trainerRepository.save(trainer);

        log.info("Trainer successfully created: ID={}, Username={}, Specialization={}",
                savedTrainer.getUser().getUserId(), savedTrainer.getUser().getUsername(), savedTrainer.getSpecialization());

        return savedTrainer;
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        log.info("Fetching trainee with Username={}", username);

        Optional<Trainer> trainer = trainerRepository.findByUsername(username);

        if (trainer.isPresent())
            log.info("Trainee found: ID={}", trainer.get().getTrainerId());
        else
            log.warn("No trainee found with Username={}", username);

        return trainer;
    }

    public List<Trainer> getUnassignedTrainers(String username) {
        return trainerRepository.findTrainersNotAssignedToTrainee(username);
    }

    public Trainer updateTrainerProfile(TrainerUpdateDto trainerUpdateDto, String username) {
        log.info("Updating trainer profile: Username={}", username);

        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Trainer not found with Username: " + username));

        // Update only the profile related fields (we do not touch User entity)
        if (trainerUpdateDto.getSpecialization() != null) {
            TrainingType specialization = trainingTypeRepository.findByTrainingTypeName(trainerUpdateDto.getSpecialization())
                    .orElseThrow(() -> new NoSuchElementException("Specialization not found: " + trainerUpdateDto.getSpecialization()));

            trainer.setSpecialization(specialization);
        }

        Trainer updatedTrainer = trainerRepository.save(trainer);
        log.info("Trainer updated successfully: Username={}, Specialization={}", updatedTrainer.getUser().getUsername(), updatedTrainer.getSpecialization());

        return updatedTrainer;
    }
}
