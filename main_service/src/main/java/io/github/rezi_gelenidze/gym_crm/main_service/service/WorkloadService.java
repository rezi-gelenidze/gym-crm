package io.github.rezi_gelenidze.gym_crm.main_service.service;


import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerWorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Training;
import io.github.rezi_gelenidze.gym_crm.main_service.enums.WorkloadUpdateType;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class WorkloadService {
    private final JmsTemplate jmsTemplate;

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

        // Send the request to the workload queue
        jmsTemplate.convertAndSend("trainer.workload.queue", request);
        log.info("Message sent to queue: {}", request);
    }

    public void fallbackNotifyWorkload(Training training, WorkloadUpdateType updateType, Throwable t) {
        log.error("Failed to notify workload-service for {}: {}. Type: {}",
                training.getTrainingName(), t.getMessage(), updateType.toString());
    }
}
