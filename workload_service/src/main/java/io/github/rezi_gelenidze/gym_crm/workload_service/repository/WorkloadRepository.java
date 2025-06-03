package io.github.rezi_gelenidze.gym_crm.workload_service.repository;

import io.github.rezi_gelenidze.gym_crm.workload_service.model.Workload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkloadRepository extends MongoRepository<Workload, String> {
    Optional<Workload> findByUsername(String username);
}
