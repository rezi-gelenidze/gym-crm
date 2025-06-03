package io.github.rezi_gelenidze.gym_crm.workload_service.controller;

import io.github.rezi_gelenidze.gym_crm.workload_service.dto.WorkloadResponse;
import io.github.rezi_gelenidze.gym_crm.workload_service.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/workloads")
public class WorkloadController {
    private final WorkloadService service;

    @GetMapping("/{username}")
    public ResponseEntity<WorkloadResponse> getFullWorkload(@PathVariable String username) {
        Optional<WorkloadResponse> response = service.getWorkload(username);

        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
