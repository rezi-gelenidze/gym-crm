package io.github.rezi_gelenidze.gym_crm.main_service.repository;

import io.github.rezi_gelenidze.gym_crm.main_service.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findByTrainingTypeName(String trainingTypeName);
}
