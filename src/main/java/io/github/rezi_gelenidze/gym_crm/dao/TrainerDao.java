package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.storage.MemoryStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDao {
    private final UserDao userDao;
    private final Map<Long, Trainer> trainerStorage;

    @Autowired
    public TrainerDao(MemoryStorage memoryStorage, UserDao userDao) {
        this.userDao = userDao;
        this.trainerStorage = memoryStorage.getTrainerStorage();
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        User user = userDao.createUser(firstName, lastName);

        Trainer trainer = new Trainer(user, specialization);

        trainerStorage.put(user.getUserId(), trainer);

        return trainer;
    }

    public Optional<Trainer> getTrainer(Long userId) {
        return Optional.ofNullable(trainerStorage.get(userId));
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerStorage.put(trainer.getUser().getUserId(), trainer);
    }
}
