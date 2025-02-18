package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDao {
    private final Map<String, Training> trainingStorage;

    @Autowired
    public TrainingDao(Map<String, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public Training saveTraining(Training training) {
        // Let key be a composite key of "{traineeId}-{trainerId}" as we assume that this M2M relationship between
        // Trainee and Trainer But one trainee does not take the same training from the same trainer
        String compositePrimaryKey = training.getTraineeId() + "-" + training.getTrainerId();
        trainingStorage.put(compositePrimaryKey, training);

        return training;
    }

    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        return Optional.ofNullable(trainingStorage.get(traineeId + "-" + trainerId));
    }
}
