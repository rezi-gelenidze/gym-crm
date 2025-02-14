package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dao.TraineeDao;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeService {
    private final TraineeDao traineeDao;

    @Autowired
    public TraineeService(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        return traineeDao.createTrainee(firstName, lastName, dateOfBirth, address);
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
