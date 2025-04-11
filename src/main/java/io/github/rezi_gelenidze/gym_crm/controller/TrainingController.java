package io.github.rezi_gelenidze.gym_crm.controller;

import io.github.rezi_gelenidze.gym_crm.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(name = "Training Management", description = "Endpoints for managing trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    @Operation(summary = "Create a new training")
    public ResponseEntity<Void> createTraining(@RequestBody TrainingCreateDto trainingCreateDto) {
        trainingService.createTraining(trainingCreateDto);
        return ResponseEntity.status(201).build();
    }
}
