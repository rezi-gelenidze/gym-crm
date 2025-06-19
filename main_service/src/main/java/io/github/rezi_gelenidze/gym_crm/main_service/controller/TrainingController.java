package io.github.rezi_gelenidze.gym_crm.main_service.controller;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.main_service.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> createTraining(@RequestBody @Valid TrainingCreateDto trainingCreateDto) {
        trainingService.createTraining(trainingCreateDto);
        return ResponseEntity.status(201).build();
    }
}
