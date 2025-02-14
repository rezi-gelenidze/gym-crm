package io.github.rezi_gelenidze.gym_crm.dao;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.storage.MemoryStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDao {
    private final Map<String, Training> trainingStorage;

    @Autowired
    public TrainingDao(MemoryStorage memoryStorage) {
        this.trainingStorage = memoryStorage.getTrainingStorage();
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, TrainingType trainingTypeName, String trainingDate, Duration trainingDuration) {
        Training training = new Training(traineeId, trainerId, trainingName, trainingTypeName, trainingDate, trainingDuration);

        // Let key be a composite key of "{traineeId}-{trainerId}" as we assume that this M2M relationship between
        // Trainee and Trainer But one trainee does not take the same training from the same trainer
        String compositePrimaryKey = traineeId + "-" + trainerId;

        trainingStorage.put(compositePrimaryKey, training);

        return training;
    }

    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        return Optional.ofNullable(trainingStorage.get(traineeId + "-" + trainerId));
    }
}
