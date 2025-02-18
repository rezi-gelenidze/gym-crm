package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDao {

    private final Map<Long, Trainer> trainerStorage;

    @Autowired
    public TrainerDao(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public Trainer saveTrainer(Trainer trainer) {
        trainerStorage.put(trainer.getUserId(), trainer);

        return trainer;
    }

    public Optional<Trainer> getTrainer(Long userId) {
        return Optional.ofNullable(trainerStorage.get(userId));
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerStorage.put(trainer.getUserId(), trainer);
    }
}
