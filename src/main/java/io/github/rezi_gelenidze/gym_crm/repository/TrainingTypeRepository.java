package io.github.rezi_gelenidze.gym_crm.repository;

import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findByTrainingTypeName(String trainingTypeName);
}
