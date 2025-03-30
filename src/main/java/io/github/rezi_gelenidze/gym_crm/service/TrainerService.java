package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerProfileDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerUpdateDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService {
    private final UserService userService;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public Map<String, String> createTrainer(TrainerCreateDto trainerCreateDto) {
        log.info("Creating new trainer: {} {}, Specialization={}",
                trainerCreateDto.getFirstName(), trainerCreateDto.getLastName(), trainerCreateDto.getSpecializationId());

        // preserve raw password to return to user (as requested in requirements)
        String rawPassword = userService.generateRawPassword();

        User newUser = new User(
                trainerCreateDto.getFirstName(),
                trainerCreateDto.getLastName(),
                userService.generateUsername(trainerCreateDto.getFirstName(), trainerCreateDto.getLastName()),
                userService.hashPassword(rawPassword)
        );

        System.out.println(trainingTypeRepository.findAll());
        TrainingType trainingType = trainingTypeRepository
                .findById(trainerCreateDto.getSpecializationId())
                .orElseThrow(NoSuchElementException::new);

        Trainer trainer = new Trainer(newUser, trainingType);

        Trainer savedTrainer = trainerRepository.save(trainer);

        log.info("Trainer successfully created: ID={}, Username={}, Specialization={}",
                savedTrainer.getUser().getUserId(), savedTrainer.getUser().getUsername(), savedTrainer.getSpecialization());

        return Map.of(
                "username", savedTrainer.getUser().getUsername(),
                "password", rawPassword
        );
    }

    public Optional<TrainerProfileDto> getTrainerProfile(String username) {
        log.info("Fetching trainer with Username={}", username);

        // query the trainer itself
        Trainer trainer = trainerRepository.findByUsername(username).orElse(null);

        if (trainer == null) return Optional.empty();

        // query associated trainees
        List<TraineeListItemDto> trainees = trainerRepository.findTrainerTrainees(username);


        TrainerProfileDto trainerProfileDto = new TrainerProfileDto(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getSpecialization().getTrainingTypeId(),
                trainer.getUser().isActive(),
                trainees
        );

        return Optional.of(trainerProfileDto);
    }

    public Optional<TrainerProfileDto> updateTrainerProfile(TrainerUpdateDto trainerUpdateDto) {
        log.info("Updating trainer with Username={}", trainerUpdateDto.getUsername());

        // query the trainer itself
        Trainer trainer = trainerRepository.findByUsername(trainerUpdateDto.getUsername()).orElse(null);

        if (trainer == null) return Optional.empty();

        trainer.getUser().setUsername(trainerUpdateDto.getUsername());
        trainer.getUser().setFirstName(trainerUpdateDto.getFirstName());
        trainer.getUser().setLastName(trainerUpdateDto.getLastName());

        Trainer updatedTrainer = trainerRepository.save(trainer);

        log.info("Trainer successfully updated: ID={}, Username={}",
                updatedTrainer.getUser().getUserId(), updatedTrainer.getUser().getUsername());

        return Optional.of(new TrainerProfileDto(
                updatedTrainer.getUser().getFirstName(),
                updatedTrainer.getUser().getLastName(),
                updatedTrainer.getSpecialization().getTrainingTypeId(),
                updatedTrainer.getUser().isActive(),
                trainerRepository.findTrainerTrainees(trainerUpdateDto.getUsername())
        ));
    }

    public List<TrainerListItemDto> getUnassignedTrainers(String username) {
        return trainerRepository.findTrainersNotAssignedToTrainee(username);
    }
}
