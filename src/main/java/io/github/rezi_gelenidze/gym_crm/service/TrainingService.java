package io.github.rezi_gelenidze.gym_crm.service;

import java.time.Duration;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.dao.TrainingDao;
import io.github.rezi_gelenidze.gym_crm.entity.Training;

import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private final TrainingDao trainingDao;

    @Autowired
    public TrainingService(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, TrainingType trainingTypeName, String trainingDate, Duration trainingDuration) {
        Training training = new Training(traineeId, trainerId, trainingName, trainingTypeName, trainingDate, trainingDuration);

        return trainingDao.saveTraining(training);
    }

    public Optional<Training> getTraining(Long traineeId, Long trainerId) {
        return trainingDao.getTraining(traineeId, trainerId);
    }
}
