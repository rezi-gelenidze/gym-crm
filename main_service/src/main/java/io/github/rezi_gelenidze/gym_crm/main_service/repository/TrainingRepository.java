package io.github.rezi_gelenidze.gym_crm.main_service.repository;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TraineeTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TrainerTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    // Filter based search methods for trainee and trainer trainings
    @Query("""
        SELECT new io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TraineeTrainingListItemDto(
            tr.trainingName,
            tr.trainingDate,
            tr.trainingType.trainingTypeId,
            tr.trainingDuration,
            tr.trainer.user.username
        )
        FROM Training tr
        WHERE tr.trainee.user.username = :traineeUsername
            AND (:fromDate IS NULL OR tr.trainingDate >= :fromDate)
            AND (:toDate IS NULL OR tr.trainingDate <= :toDate)
            AND (:trainerName IS NULL OR tr.trainer.user.username = :trainerName)
            AND (:trainingTypeId IS NULL OR tr.trainingType.trainingTypeId = :trainingTypeId)
    """)
    List<TraineeTrainingListItemDto> findTraineeTrainings(
            @Param("traineeUsername") String traineeUsername,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingTypeId") Long trainingTypeId);


    @Query("""
        SELECT new io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TrainerTrainingListItemDto(
            tr.trainingName,
            tr.trainingDate,
            tr.trainingType.trainingTypeId,
            tr.trainingDuration,
            tr.trainee.user.username
        )
        FROM Training tr
        WHERE tr.trainer.user.username = :username
            AND (:fromDate IS NULL OR tr.trainingDate >= :fromDate)
            AND (:toDate IS NULL OR tr.trainingDate <= :toDate)
            AND (:traineeName IS NULL OR tr.trainee.user.username = :traineeName)
    """)
    List<TrainerTrainingListItemDto> findTrainerTrainings(
            @Param("username") String trainerUsername,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("traineeName") String traineeName);

}
