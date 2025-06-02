package io.github.rezi_gelenidze.gym_crm.main_service.client;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "workload-service")
public interface WorkloadClient {
    @PostMapping("/workloads")
    ResponseEntity<Void> sendWorkload(@RequestBody TrainerWorkloadRequest request);
}
