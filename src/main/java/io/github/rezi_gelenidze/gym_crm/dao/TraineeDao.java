package io.github.rezi_gelenidze.gym_crm.dao;

import java.util.Map;
import java.util.Optional;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.User;
import io.github.rezi_gelenidze.gym_crm.storage.MemoryStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDao {
    private final UserDao userDao;
    private final Map<Long, Trainee> traineeStorage;

    @Autowired
    public TraineeDao(MemoryStorage memoryStorage, UserDao userDao) {
        this.userDao = userDao;
        this.traineeStorage = memoryStorage.getTraineeStorage();
    }

    public Trainee createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        // Generate user class as parent in vertically partitioned entity organization
        User user = userDao.createUser(firstName, lastName);

        // create trainee object and store it in the map
        Trainee trainee = new Trainee(user, dateOfBirth, address);

        traineeStorage.put(user.getUserId(), trainee);

        return trainee;
    }

    public Optional<Trainee> getTrainee(Long userId) {
        return Optional.ofNullable(traineeStorage.get(userId));
    }

    public boolean deleteTrainee(Long userId) {
        return traineeStorage.remove(userId) != null;
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeStorage.put(trainee.getUser().getUserId(), trainee);
    }
}
