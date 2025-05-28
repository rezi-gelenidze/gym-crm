package io.github.rezi_gelenidze.gym_crm.main_service.controller;

import io.github.rezi_gelenidze.gym_crm.main_service.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.main_service.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/training-types")
@RequiredArgsConstructor
@Tag(name = "Training Type Management", description = "Endpoints for retrieving training types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @GetMapping
    @Operation(summary = "Get all training types")
    public ResponseEntity<List<TrainingType>> getAllTrainingTypes() {
        return ResponseEntity.ok(trainingTypeService.getAllTrainingTypes());
    }
}
