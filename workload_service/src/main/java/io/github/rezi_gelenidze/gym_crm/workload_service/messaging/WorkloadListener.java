package io.github.rezi_gelenidze.gym_crm.workload_service.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rezi_gelenidze.gym_crm.workload_service.dto.WorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.workload_service.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkloadListener {
    private final WorkloadService trainerWorkloadService;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = "trainer.workload.queue")
    public void receive(String payload) throws Exception {
        // Manually convert JSON to your local class, instead of shared class in shared module
        WorkloadRequest request = objectMapper.readValue(
                payload, WorkloadRequest.class
        );
        log.info("Received trainer workload request: {}", request);

        // Process the request
        trainerWorkloadService.recordWorkload(request);
    }
}
