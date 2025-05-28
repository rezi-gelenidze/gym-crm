package io.github.rezi_gelenidze.gym_crm.main_service.repository;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerListItemDto;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.rezi_gelenidze.gym_crm.main_service.entity.Trainee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    @Query("SELECT t FROM Trainee t WHERE t.user.username = :username")
    Optional<Trainee> findByUsername(@Param("username") String username);

    @Query("""
        SELECT new io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerListItemDto(
            trr.user.username,
            trr.user.firstName,
            trr.user.lastName,
            trr.specialization.trainingTypeId
        )
        FROM Training tr
        JOIN tr.trainee tre
        JOIN tr.trainer trr
        WHERE tre.user.username = :traineeUsername
        """)
    List<TrainerListItemDto> findTraineeTrainers(@Param("traineeUsername") String traineeUsername);


    @Modifying
    @Query("DELETE FROM Trainee t WHERE t.user.username = :username")
    void deleteByUsername(String username);
}
