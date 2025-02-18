package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDao {
    private final Map<Long, Trainee> traineeStorage;

    @Autowired
    public TraineeDao(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public Trainee saveTrainee(Trainee trainee) {
        traineeStorage.put(trainee.getUserId(), trainee);

        return trainee;
    }

    public Optional<Trainee> getTrainee(Long userId) {
        return Optional.ofNullable(traineeStorage.get(userId));
    }

    public boolean deleteTrainee(Long userId) {
        return traineeStorage.remove(userId) != null;
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeStorage.put(trainee.getUserId(), trainee);
    }
}
