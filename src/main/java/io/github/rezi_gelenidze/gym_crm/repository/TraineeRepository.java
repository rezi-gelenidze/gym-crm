package io.github.rezi_gelenidze.gym_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    @Query("SELECT t FROM Trainee t WHERE t.user.username = :username")
    Optional<Trainee> findByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM Trainee t WHERE t.user.username = :username")
    void deleteByUsername(String username);
}
