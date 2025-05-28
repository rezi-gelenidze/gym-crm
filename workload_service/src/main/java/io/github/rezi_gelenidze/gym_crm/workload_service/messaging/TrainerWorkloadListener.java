package io.github.rezi_gelenidze.gym_crm.workload_service.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rezi_gelenidze.gym_crm.workload_service.dto.TrainerWorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.workload_service.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadListener {
    private final TrainerWorkloadService trainerWorkloadService;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = "trainer.workload.queue")
    public void receive(String payload) throws Exception {
        // Manually convert JSON to your local class, instead of shared class in shared module
        TrainerWorkloadRequest request = objectMapper.readValue(
                payload, TrainerWorkloadRequest.class
        );
        log.info("Received trainer workload request: {}", request);

        // Process the request
        trainerWorkloadService.recordWorkload(request);
    }
}
