package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TrainerDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Trainer trainer = new Trainer(
                userService.generateTrainerId(),
                firstName,
                lastName,
                userService.generateUsername(firstName, lastName),
                userService.generatePassword(),
                specialization
        );

        return trainerDao.saveTrainer(trainer);
    }

    public Optional<Trainer> getTrainer(Long userId) {
        return trainerDao.getTrainer(userId);
    }

    public Trainer updateTrainer(Trainer Trainer) {
        return trainerDao.updateTrainer(Trainer);
    }
}
