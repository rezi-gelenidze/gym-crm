package io.github.rezi_gelenidze.gym_crm.workload_service.controller;

import io.github.rezi_gelenidze.gym_crm.workload_service.dto.TrainerWorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.workload_service.dto.TrainerWorkloadResponse;
import io.github.rezi_gelenidze.gym_crm.workload_service.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/workloads")
public class TrainerWorkloadController {
    private final TrainerWorkloadService service;

    @PostMapping
    public ResponseEntity<String> recordWorkload(@RequestBody TrainerWorkloadRequest request) {
        service.recordWorkload(request);
        return ResponseEntity.ok("Workload recorded successfully.");
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerWorkloadResponse> getFullWorkload(@PathVariable String username) {
        TrainerWorkloadResponse response = service.getWorkload(username);
        return ResponseEntity.ok(response);
    }

}
