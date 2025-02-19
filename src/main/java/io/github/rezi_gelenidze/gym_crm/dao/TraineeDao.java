package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/*
    * This class is responsible for storing and managing Trainee objects.
    * It provides basic CRUD operation in memory-storage represented by
    * a Map<Long, Trainee> traineeStorage field as injected bean.
 */
@Repository
public class TraineeDao {
    private Map<Long, Trainee> traineeStorage;

    // Setter injection for traineeStorage (according to task requirements)
    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    /*
        * This method is responsible for saving Trainee object in storage.
        *
        * @param trainee - Trainee object to be saved
        * @return Trainee - Trainee object that was saved
        *
        * @throws IllegalArgumentException if Trainee object with same ID already exists
     */
    public Trainee saveTrainee(Trainee trainee) {
        if (traineeStorage.containsKey(trainee.getUserId()))
            throw new IllegalArgumentException("Trainee with ID " + trainee.getUserId() + " already exists");

        traineeStorage.put(trainee.getUserId(), trainee);
        return trainee;
    }

    /*
        * This method is responsible for fetching Trainee object from storage.
        *
        * @param userId - ID of Trainee object to be fetched
        * @return Optional<Trainee> - Trainee object if found, empty Optional otherwise
     */
    public Optional<Trainee> getTrainee(Long userId) {
        return Optional.ofNullable(traineeStorage.get(userId));
    }

    /*
        * This method is responsible for deleting Trainee object from storage.
        *
        * @param userId - ID of Trainee object to be deleted
        * @return boolean - true if Trainee object was deleted successfully, false otherwise
     */
    public boolean deleteTrainee(Long userId) {
        return traineeStorage.remove(userId) != null;
    }

    /*
        * This method is responsible for updating Trainee object in storage.
        *
        * @param trainee - Trainee object to be updated
        * @return Trainee - Trainee object that was updated
        *
        * @throws IllegalArgumentException if Trainee object with same ID does not exist
     */
    public Trainee updateTrainee(Trainee trainee) {
        if (!traineeStorage.containsKey(trainee.getUserId()))
            throw new IllegalArgumentException("Trainee with ID " + trainee.getUserId() + " does not exist");

        traineeStorage.put(trainee.getUserId(), trainee);
        return trainee;
    }
}
