package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TraineeDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TraineeService {
    private UserService userService;
    private TraineeDao traineeDao;

    // Setter injections (according to task requirements)
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        log.info("Creating new trainee: {} {}, Date of Birth={}, Address={}", firstName, lastName, dateOfBirth, address);

        Trainee trainee = new Trainee(
                userService.generateTraineeId(),
                firstName,
                lastName,
                userService.generateUsername(firstName, lastName),
                userService.generatePassword(),
                dateOfBirth,
                address
        );

        Trainee savedTrainee = traineeDao.saveTrainee(trainee);

        log.info("Trainee successfully created: ID={}, Username={}, Address={}",
                savedTrainee.getUserId(), savedTrainee.getUsername(), savedTrainee.getAddress());

        return savedTrainee;
    }

    public Optional<Trainee> getTrainee(Long userId) {
        log.info("Fetching trainee with ID={}", userId);

        Optional<Trainee> trainee = traineeDao.getTrainee(userId);

        if (trainee.isPresent()) {
            log.info("Trainee found: ID={}, Username={}, Address={}",
                    trainee.get().getUserId(), trainee.get().getUsername(), trainee.get().getAddress());
        } else {
            log.warn("No trainee found with ID={}", userId);
        }

        return trainee;
    }

    public boolean deleteTrainee(Long userId) {
        log.info("Deleting trainee with ID={}", userId);

        boolean deleted = traineeDao.deleteTrainee(userId);

        if (deleted) {
            log.info("Trainee successfully deleted: ID={}", userId);
        } else {
            log.warn("Failed to delete trainee: ID {} does not exist", userId);
        }

        return deleted;
    }

    public Trainee updateTrainee(Trainee trainee) {
        log.info("Updating trainee: ID={}, Username={}, New Address={}",
                trainee.getUserId(), trainee.getUsername(), trainee.getAddress());

        Trainee updatedTrainee = traineeDao.updateTrainee(trainee);

        log.info("Trainee successfully updated: ID={}, New Address={}",
                updatedTrainee.getUserId(), updatedTrainee.getAddress());

        return updatedTrainee;
    }
}
