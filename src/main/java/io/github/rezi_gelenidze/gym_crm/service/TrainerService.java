package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TrainerDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerService {
    private final TrainerDao trainerDao;

    @Autowired
    public TrainerService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        return trainerDao.createTrainer(firstName, lastName, specialization);
    }

    public Optional<Trainer> getTrainer(Long userId) {
        return trainerDao.getTrainer(userId);
    }

    public Trainer updateTrainer(Trainer Trainer) {
        return trainerDao.updateTrainer(Trainer);
    }
}
