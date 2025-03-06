package io.github.rezi_gelenidze.gym_crm.repository;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t WHERE t.user.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);

    // Select all trainers that are not in trainings assigned to the given trainee
    @Query("SELECT tr FROM Trainer tr WHERE tr NOT IN (" +
            "SELECT t.trainer FROM Training t WHERE t.trainee.user.username = :traineeUsername)")
    List<Trainer> findTrainersNotAssignedToTrainee(@Param("traineeUsername") String traineeUsername);
}
