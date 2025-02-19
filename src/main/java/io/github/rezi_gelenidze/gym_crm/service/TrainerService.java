package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TrainerDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TrainerService {
    private UserService userService;
    private TrainerDao trainerDao;

    // Setter injections (according to task requirements)
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        log.info("Creating new trainer: {} {}, Specialization={}", firstName, lastName, specialization);

        Trainer trainer = new Trainer(
                userService.generateTrainerId(),
                firstName,
                lastName,
                userService.generateUsername(firstName, lastName),
                userService.generatePassword(),
                specialization
        );

        Trainer savedTrainer = trainerDao.saveTrainer(trainer);

        log.info("Trainer successfully created: ID={}, Username={}, Specialization={}",
                savedTrainer.getUserId(), savedTrainer.getUsername(), savedTrainer.getSpecialization());

        return savedTrainer;
    }

    public Optional<Trainer> getTrainer(Long userId) {
        log.info("Fetching trainer with ID={}", userId);

        Optional<Trainer> trainer = trainerDao.getTrainer(userId);

        if (trainer.isPresent()) {
            log.info("Trainer found: ID={}, Username={}, Specialization={}",
                    trainer.get().getUserId(), trainer.get().getUsername(), trainer.get().getSpecialization());
        } else {
            log.warn("No trainer found with ID={}", userId);
        }

        return trainer;
    }

    public Trainer updateTrainer(Trainer trainer) {
        log.info("Updating trainer: ID={}, Username={}, Specialization={}",
                trainer.getUserId(), trainer.getUsername(), trainer.getSpecialization());

        Trainer updatedTrainer = trainerDao.updateTrainer(trainer);

        log.info("Trainer successfully updated: ID={}, New Specialization={}",
                updatedTrainer.getUserId(), updatedTrainer.getSpecialization());

        return updatedTrainer;
    }
}
