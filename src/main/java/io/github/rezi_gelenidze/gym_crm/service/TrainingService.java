package io.github.rezi_gelenidze.gym_crm.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import io.github.rezi_gelenidze.gym_crm.dto.training.TraineeTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainerTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.exception.UserNotFoundException;
import io.github.rezi_gelenidze.gym_crm.repository.TraineeRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;

    public Training createTraining(TrainingCreateDto trainingCreateDto) {

        Trainee trainee = traineeRepository.findByUsername(trainingCreateDto.getTraineeUsername())
                .orElseThrow(UserNotFoundException::new);

        Trainer trainer = trainerRepository.findByUsername(trainingCreateDto.getTrainerUsername())
                .orElseThrow(UserNotFoundException::new);

        Training training = new Training(
                trainee,
                trainer,
                trainer.getSpecialization(), // Infer training type from trainer specialization
                trainingCreateDto.getTrainingName(),
                trainingCreateDto.getTrainingDate(),
                trainingCreateDto.getTrainingDuration()
        );

        Training savedTraining = trainingRepository.save(training);

        // Log Success
        log.info("Training successfully created: ID={}, Trainee={}, Trainer={}, Training Type={}, Training Name={}, Training Date={}, Training Duration={}",
                savedTraining.getId(),
                savedTraining.getTrainee().getUser().getUsername(),
                savedTraining.getTrainer().getUser().getUsername(),
                savedTraining.getTrainingType().getTrainingTypeName(),
                savedTraining.getTrainingName(),
                savedTraining.getTrainingDate(),
                savedTraining.getTrainingDuration());

        return savedTraining;
    }

    public List<TraineeTrainingListItemDto> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, Long trainingTypeId) {
        return trainingRepository.findTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);
    }

    public List<TrainerTrainingListItemDto> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        return trainingRepository.findTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }
}
