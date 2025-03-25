package io.github.rezi_gelenidze.gym_crm.controller;

import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.service.TrainingTypeService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
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
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all training types")
    public ResponseEntity<List<TrainingType>> getAllTrainingTypes(
            @RequestHeader("X-Username") String authUsername,
            @RequestHeader("X-Password") String authPassword) {

        userService.authenticate(authUsername, authPassword);

        return ResponseEntity.ok(trainingTypeService.getAllTrainingTypes());
    }
}
