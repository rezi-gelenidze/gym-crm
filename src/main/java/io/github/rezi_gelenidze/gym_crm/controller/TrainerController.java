package io.github.rezi_gelenidze.gym_crm.controller;

import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerProfileDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerUpdateDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainerTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.service.TrainerService;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainer Management", description = "Endpoints for managing trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new trainer")
    public ResponseEntity<Map<String, String>> createTrainer(@RequestBody TrainerCreateDto trainerCreateDto) {
        return ResponseEntity.status(201).body(trainerService.createTrainer(trainerCreateDto));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainer profile")
    public ResponseEntity<TrainerProfileDto> getTrainerProfile(
            @PathVariable String username) {
        Optional<TrainerProfileDto> trainer = trainerService.getTrainerProfile(username);
        return trainer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer profile")
    public ResponseEntity<TrainerProfileDto> updateTrainerProfile(
            @PathVariable String username,
            @RequestBody TrainerUpdateDto trainerUpdateDto) {

        Optional<TrainerProfileDto> updatedTrainer = trainerService.updateTrainerProfile(trainerUpdateDto);

        return updatedTrainer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping("/{username}")
    @Operation(summary = "Activate or Deactivate a trainer")
    public ResponseEntity<Void> activateDeactivateTrainer(
            @PathVariable String username,
            @RequestParam boolean isActive) {

        userService.updateActiveStatus(username, isActive);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer's training list")
    public ResponseEntity<List<TrainerTrainingListItemDto>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String traineeName) {
        return ResponseEntity.ok(trainingService.getTrainerTrainings(username, from, to, traineeName));
    }
}
