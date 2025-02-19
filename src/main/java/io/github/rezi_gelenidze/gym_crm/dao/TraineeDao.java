package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*
 * This class is responsible for storing and managing Trainee objects.
 * It provides basic CRUD operations in memory-storage represented by
 * a Map<Long, Trainee> traineeStorage field as an injected bean.
 */
@Slf4j
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
        if (traineeStorage.containsKey(trainee.getUserId())) {
            log.warn("Failed to save trainee: ID {} already exists", trainee.getUserId());
            throw new IllegalArgumentException("Trainee with ID " + trainee.getUserId() + " already exists");
        }

        traineeStorage.put(trainee.getUserId(), trainee);
        log.info("Trainee saved: ID={}, Username={}", trainee.getUserId(), trainee.getUsername());
        return trainee;
    }

    /*
     * This method is responsible for fetching a Trainee object from storage.
     *
     * @param userId - ID of Trainee object to be fetched
     * @return Optional<Trainee> - Trainee object if found, empty Optional otherwise
     */
    public Optional<Trainee> getTrainee(Long userId) {
        Optional<Trainee> trainee = Optional.ofNullable(traineeStorage.get(userId));
        if (trainee.isPresent()) {
            log.info("Fetched trainee: ID={}, Username={}", userId, trainee.get().getUsername());
        } else {
            log.warn("Trainee with ID {} not found", userId);
        }
        return trainee;
    }

    /*
     * This method is responsible for deleting a Trainee object from storage.
     *
     * @param userId - ID of Trainee object to be deleted
     * @return boolean - true if Trainee object was deleted successfully, false otherwise
     */
    public boolean deleteTrainee(Long userId) {
        boolean removed = traineeStorage.remove(userId) != null;
        if (removed) {
            log.info("Trainee with ID {} successfully deleted", userId);
        } else {
            log.warn("Failed to delete trainee: ID {} does not exist", userId);
        }
        return removed;
    }

    /*
     * This method is responsible for updating a Trainee object in storage.
     *
     * @param trainee - Trainee object to be updated
     * @return Trainee - Trainee object that was updated
     *
     * @throws IllegalArgumentException if Trainee object with same ID does not exist
     */
    public Trainee updateTrainee(Trainee trainee) {
        if (!traineeStorage.containsKey(trainee.getUserId())) {
            log.warn("Failed to update trainee: ID {} does not exist", trainee.getUserId());
            throw new IllegalArgumentException("Trainee with ID " + trainee.getUserId() + " does not exist");
        }

        traineeStorage.put(trainee.getUserId(), trainee);
        log.info("Trainee updated: ID={}, New Address={}", trainee.getUserId(), trainee.getAddress());
        return trainee;
    }
}
