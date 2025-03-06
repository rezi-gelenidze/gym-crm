package io.github.rezi_gelenidze.gym_crm.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.dto.TrainingDto;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
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
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingRepository trainingRepository;

    public Training createTraining(TrainingDto trainingDto) {

        Trainee trainee = traineeRepository.findById(trainingDto.getTraineeId())
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + trainingDto.getTraineeId()));

        Trainer trainer = trainerRepository.findById(trainingDto.getTrainerId())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + trainingDto.getTrainerId()));

        TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(trainingDto.getTrainingTypeName())
                .orElseThrow(() -> new NoSuchElementException("Training Type not found: " + trainingDto.getTrainingTypeName()));

        Training training = new Training(
                trainee,
                trainer,
                trainingType,
                trainingDto.getTrainingName(),
                trainingDto.getTrainingDate(),
                trainingDto.getTrainingDuration()
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

    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        return trainingRepository.findTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        return trainingRepository.findTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }
}
