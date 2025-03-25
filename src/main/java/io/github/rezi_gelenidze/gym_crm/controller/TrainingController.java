package io.github.rezi_gelenidze.gym_crm.controller;

import io.github.rezi_gelenidze.gym_crm.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
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
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new training")
    public ResponseEntity<Void> createTraining(
            @RequestHeader("X-Username") String authUsername,
            @RequestHeader("X-Password") String authPassword,
            @RequestBody TrainingCreateDto trainingCreateDto) {

        userService.authenticate(authUsername, authPassword);
        // Suppose only trainers can create trainings
        userService.assertIdentity(authUsername, trainingCreateDto.getTrainerUsername());

        trainingService.createTraining(trainingCreateDto);
        return ResponseEntity.status(201).build();
    }
}
