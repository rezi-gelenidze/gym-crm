package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*
 * This class is responsible for storing and managing Training objects.
 * It provides basic CRUD operations in an in-memory storage represented by
 * a Map<String, Training> trainingStorage field as an injected bean.
 */
@Slf4j
@Repository
public class TrainingDao {
    private Map<String, Training> trainingStorage;

    // Setter injection for trainingStorage (according to task requirements)
    @Autowired
    public void setTrainingStorage(Map<String, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    /*
     * This method is responsible for saving a Training object in storage.
     * @param training - Training object to be saved
     * @return Training - Training object that was saved
     *
     * @throws IllegalArgumentException if Training object with the same ID already exists
     *
     * NOTE: We assume that one trainee does not take the same training from the same trainer
     * so we use a composite key "{traineeId}-{trainerId}" as a key.
     */
    public Training saveTraining(Training training) {
        String compositePrimaryKey = training.getTraineeId() + "-" + training.getTrainerId();

        if (trainingStorage.containsKey(compositePrimaryKey)) {
            log.warn("Failed to save training: Training with ID {} already exists", compositePrimaryKey);
            throw new IllegalArgumentException("Training with ID " + compositePrimaryKey + " already exists");
        }

        trainingStorage.put(compositePrimaryKey, training);
        log.info("Training saved: ID={}, Type={}, TraineeID={}, TrainerID={}",
                compositePrimaryKey, training.getTrainingTypeName().getTrainingTypeName(),
                training.getTraineeId(), training.getTrainerId());
        return training;
    }

    /*
     * This method is responsible for fetching a Training object from storage.
     * @param traineeId - ID of the Trainee object to be fetched
     * @param trainerId - ID of the Trainer object to be fetched
     * @return Optional<Training> - Training object if found, empty Optional otherwise
     */
    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        String compositePrimaryKey = traineeId + "-" + trainerId;
        Optional<Training> training = Optional.ofNullable(trainingStorage.get(compositePrimaryKey));

        if (training.isPresent()) {
            log.info("Fetched training: ID={}, Type={}, TraineeID={}, TrainerID={}",
                    compositePrimaryKey, training.get().getTrainingTypeName().getTrainingTypeName(),
                    traineeId, trainerId);
        } else {
            log.warn("Training with ID {} not found", compositePrimaryKey);
        }

        return training;
    }
}
