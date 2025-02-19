package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*
 * This class is responsible for storing and managing Trainer objects.
 * It provides basic CRU operations in an in-memory storage represented by
 * a Map<Long, Trainer> trainerStorage field as an injected bean.
 */
@Slf4j
@Repository
public class TrainerDao {

    private Map<Long, Trainer> trainerStorage;

    // Setter injection for trainerStorage (according to task requirements)
    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    /*
     * This method is responsible for saving a Trainer object in storage.
     * @param trainer - Trainer object to be saved
     * @return Trainer - Trainer object that was saved
     *
     * @throws IllegalArgumentException if a Trainer object with the same ID already exists
     */
    public Trainer saveTrainer(Trainer trainer) {
        if (trainerStorage.containsKey(trainer.getUserId())) {
            log.warn("Failed to save trainer: ID {} already exists", trainer.getUserId());
            throw new IllegalArgumentException("Trainer with ID " + trainer.getUserId() + " already exists");
        }

        trainerStorage.put(trainer.getUserId(), trainer);
        log.info("Trainer saved: ID={}, Username={}, Specialization={}", trainer.getUserId(), trainer.getUsername(), trainer.getSpecialization());
        return trainer;
    }

    /*
     * This method is responsible for fetching a Trainer object from storage.
     * @param userId - ID of the Trainer object to be fetched
     * @return Optional<Trainer> - Optional with Trainer object if found, empty Optional otherwise
     */
    public Optional<Trainer> getTrainer(Long userId) {
        Optional<Trainer> trainer = Optional.ofNullable(trainerStorage.get(userId));
        if (trainer.isPresent()) {
            log.info("Fetched trainer: ID={}, Username={}, Specialization={}", userId, trainer.get().getUsername(), trainer.get().getSpecialization());
        } else {
            log.warn("Trainer with ID {} not found", userId);
        }
        return trainer;
    }

    /*
     * This method is responsible for updating a Trainer object in storage.
     * @param trainer - Trainer object to be updated
     * @return Trainer - Updated Trainer object
     *
     * @throws IllegalArgumentException if a Trainer object with the same ID does not exist
     */
    public Trainer updateTrainer(Trainer trainer) {
        if (!trainerStorage.containsKey(trainer.getUserId())) {
            log.warn("Failed to update trainer: ID {} does not exist", trainer.getUserId());
            throw new IllegalArgumentException("Trainer with ID " + trainer.getUserId() + " does not exist");
        }

        trainerStorage.put(trainer.getUserId(), trainer);
        log.info("Trainer updated: ID={}, New Specialization={}", trainer.getUserId(), trainer.getSpecialization());
        return trainer;
    }
}
