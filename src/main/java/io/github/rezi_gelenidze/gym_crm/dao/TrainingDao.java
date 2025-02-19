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

    /*
        * This method is responsible for saving Training object in storage.
        * @param training - Training object to be saved
        * @return Training - Training object that was saved
        *
        * @throws IllegalArgumentException if Training object with same ID already exists
        *
        * NOTE: We assume that one trainee does not take the same training from the same trainer
        * so we use composite key "{traineeId}-{trainerId}" as a key
     */
    public Training saveTraining(Training training) {
        String compositePrimaryKey = training.getTraineeId() + "-" + training.getTrainerId();

        if (trainingStorage.containsKey(compositePrimaryKey))
            throw new IllegalArgumentException("Training with ID " + compositePrimaryKey + " already exists");

        trainingStorage.put(compositePrimaryKey, training);
        return training;
    }


    /*
        * This method is responsible for fetching Training object from storage.
        * @param traineeId - ID of Trainee object to be fetched
        * @param trainerId - ID of Trainer object to be fetched
        * @return Optional<Training> - Training object if found, empty Optional otherwise
     */
    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        return Optional.ofNullable(trainingStorage.get(traineeId + "-" + trainerId));
    }
}
