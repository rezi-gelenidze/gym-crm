package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TrainerDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerService {
    private final UserService userService;
    private final TrainerDao trainerDao;

    @Autowired
    public TrainerService(UserService userService, TrainerDao trainerDao) {
        this.userService = userService;
        this.trainerDao = trainerDao;
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        Trainer trainer = new Trainer();
        trainer.setSpecialization(specialization);

        // User base fields
        trainer.setUserId(userService.generateTrainerId());
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setUsername(userService.generateUsername(firstName, lastName));
        trainer.setPassword(userService.generatePassword());

        return trainerDao.saveTrainer(trainer);
    }

    public Optional<Trainer> getTrainer(Long userId) {
        return trainerDao.getTrainer(userId);
    }

    public Trainer updateTrainer(Trainer Trainer) {
        return trainerDao.updateTrainer(Trainer);
    }
}
