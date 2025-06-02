package io.github.rezi_gelenidze.gym_crm.main_service.service;


import io.github.rezi_gelenidze.gym_crm.main_service.client.WorkloadClient;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerWorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Training;
import io.github.rezi_gelenidze.gym_crm.main_service.enums.WorkloadUpdateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class WorkloadService {
    private final WorkloadClient workloadClient;

    @CircuitBreaker(name = "workloadService", fallbackMethod = "fallbackNotifyWorkload")
    @Retry(name = "workloadService")
    public void notifyWorkloadService(Training training, WorkloadUpdateType updateType) {
        var request = new TrainerWorkloadRequest(
                training.getTrainer().getUser().getUsername(),
                training.getTrainer().getUser().getFirstName(),
                training.getTrainer().getUser().getLastName(),
                training.getTrainer().getUser().isActive(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                updateType
        );

        workloadClient.sendWorkload(request);
    }

    public void fallbackNotifyWorkload(Training training, WorkloadUpdateType updateType, Throwable t) {
        log.error("Failed to notify workload-service for {} of update type {}: {}",
                training.getTrainingName(), updateType.toString(), t.getMessage());
    }
}
