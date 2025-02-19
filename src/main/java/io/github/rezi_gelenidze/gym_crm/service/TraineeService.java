package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TraineeDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Trainee trainee = new Trainee(
                userService.generateTraineeId(),
                firstName,
                lastName,
                userService.generateUsername(firstName, lastName),
                userService.generatePassword(),
                dateOfBirth,
                address
        );

        return traineeDao.saveTrainee(trainee);
    }

    public Optional<Trainee> getTrainee(Long userId) {
        return traineeDao.getTrainee(userId);
    }

    public boolean deleteTrainee(Long userId) {
        return traineeDao.deleteTrainee(userId);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeDao.updateTrainee(trainee);
    }
}
