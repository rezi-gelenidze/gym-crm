package io.github.rezi_gelenidze.gym_crm.repository;

import io.github.rezi_gelenidze.gym_crm.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    // Criteria based search methods for trainee and trainer trainings
    @Query("SELECT t FROM Training t WHERE " +
            "t.trainee.user.username = ?1 " +
            "AND (:fromDate IS NULL OR t.trainingDate >= ?2) " +
            "AND (:toDate IS NULL OR t.trainingDate <= ?3) " +
            "AND (:trainerName IS NULL OR t.trainer.user.username = ?4) " +
            "AND (:trainingType IS NULL OR t.trainingType.trainingTypeName = ?5)")
    List<Training> findTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType);


    @Query("SELECT t FROM Training t WHERE " +
            "t.trainer.user.username = ?1 " +
            "AND (:fromDate IS NULL OR t.trainingDate >= ?2) " +
            "AND (:toDate IS NULL OR t.trainingDate <= ?3) " +
            "AND (:traineeName IS NULL OR t.trainee.user.username = ?4)")
    List<Training> findTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName);
}
