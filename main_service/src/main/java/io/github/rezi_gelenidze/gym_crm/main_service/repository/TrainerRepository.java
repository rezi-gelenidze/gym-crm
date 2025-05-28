package io.github.rezi_gelenidze.gym_crm.main_service.repository;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainee.TraineeListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t WHERE t.user.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);

    @Query("""
        SELECT new io.github.rezi_gelenidze.gym_crm.main_service.dto.trainee.TraineeListItemDto(
            tre.user.username,
            tre.user.firstName,
            tre.user.lastName
        )
        FROM Training tr
        JOIN tr.trainee tre
        JOIN tr.trainer trr
        WHERE trr.user.username = :trainerUsername
        """)
    List<TraineeListItemDto> findTrainerTrainees(@Param("trainerUsername") String trainerUsername);

    // Select all trainers that are not in trainings assigned to the given trainee
    @Query("""
        SELECT new io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerListItemDto(
            tr.user.username,
            tr.user.firstName,
            tr.user.lastName,
            tr.specialization.trainingTypeId
        )
        FROM Trainer tr 
        WHERE tr NOT IN (
            SELECT t.trainer FROM Training t WHERE t.trainee.user.username = :traineeUsername
        )
    """)
    List<TrainerListItemDto> findTrainersNotAssignedToTrainee(@Param("traineeUsername") String traineeUsername);
}
