package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*
 * This class is responsible for storing and managing Trainer objects.
 * It provides basic CRU operation in memory-storage represented by
 * a Map<Long, Trainer> trainerStorage field as injected bean.
 */
@Repository
public class TrainerDao {

    private Map<Long, Trainer> trainerStorage;

    // Setter injection for trainerStorage (according to task requirements)
    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    /*
        * This method is responsible for saving Trainer object in storage.
        * @param trainer - Trainer object to be saved
        * @return Trainer - Trainer object that was saved
        *
        * @throws IllegalArgumentException if Trainer object with same ID already exists
     */
    public Trainer saveTrainer(Trainer trainer) {
        if (trainerStorage.containsKey(trainer.getUserId()))
            throw new IllegalArgumentException("Trainer with ID " + trainer.getUserId() + " already exists");

        trainerStorage.put(trainer.getUserId(), trainer);
        return trainer;
    }

    /*
        * This method is responsible for fetching Trainer object from storage.
        * @param userId - ID of Trainer object to be fetched
        * @return Optional<Trainer> - Optional with Trainer object if found, empty Optional otherwise
     */
    public Optional<Trainer> getTrainer(Long userId) {
        return Optional.ofNullable(trainerStorage.get(userId));
    }

    /*
        * This method is responsible for updating Trainer object in storage.
        * @param trainer - Trainer object to be updated
        *
     */
    public Trainer updateTrainer(Trainer trainer) {
        if (!trainerStorage.containsKey(trainer.getUserId()))
            throw new IllegalArgumentException("Trainer with ID " + trainer.getUserId() + " does not exist");

        trainerStorage.put(trainer.getUserId(), trainer);
        return trainer;
    }
}
