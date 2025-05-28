package io.github.rezi_gelenidze.gym_crm.main_service.service;

import java.time.LocalDate;
import java.util.List;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TraineeTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TrainerTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Training;
import io.github.rezi_gelenidze.gym_crm.main_service.exception.UserNotFoundException;
import io.github.rezi_gelenidze.gym_crm.main_service.repository.TraineeRepository;
import io.github.rezi_gelenidze.gym_crm.main_service.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.main_service.repository.TrainingRepository;
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
    private final WorkloadService workloadService;

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

        // Notify Workload Service
        workloadService.notifyWorkloadService(savedTraining, "ADD");


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
