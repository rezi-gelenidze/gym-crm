package io.github.rezi_gelenidze.gym_crm.main_service.controller;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainee.TraineeCreateDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainee.TraineeProfileDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainee.TraineeUpdateDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TraineeTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.main_service.service.TraineeService;
import io.github.rezi_gelenidze.gym_crm.main_service.service.TrainerService;
import io.github.rezi_gelenidze.gym_crm.main_service.service.TrainingService;
import io.github.rezi_gelenidze.gym_crm.main_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
@Tag(name = "Trainee Management", description = "Endpoints for managing trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainingService trainingService;

    @PostMapping
    @Operation(summary = "Create a new trainee")
    public ResponseEntity<Map<String, String>> createTrainee(@RequestBody TraineeCreateDto traineeCreateDto) {
        return ResponseEntity.status(201).body(traineeService.createTrainee(traineeCreateDto));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee profile")
    public ResponseEntity<TraineeProfileDto> getTraineeProfile(
            @PathVariable String username) {
        Optional<TraineeProfileDto> trainee = traineeService.getTraineeProfile(username);
        return trainee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(("/{username}"))
    @Operation(summary = "Update trainee profile")
    public ResponseEntity<TraineeProfileDto> updateTraineeProfile(
            @PathVariable String username,
            @RequestBody TraineeUpdateDto traineeUpdateDto) {

        Optional<TraineeProfileDto> updatedTrainee = traineeService.updateTraineeProfile(traineeUpdateDto);

        return updatedTrainee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{username}")
    @Operation(summary = "Activate or Deactivate a trainee")
    public ResponseEntity<Void> activateDeactivateTrainee(
            @PathVariable String username,
            @RequestParam boolean isActive) {
        userService.updateActiveStatus(username, isActive);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile")
    public ResponseEntity<Void> deleteTrainee(
            @PathVariable String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainee's trainings list")
    public ResponseEntity<List<TraineeTrainingListItemDto>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) Long trainingTypeId) {

        return ResponseEntity.ok(trainingService.getTraineeTrainings(username, from, to, trainerName, trainingTypeId));
    }

    @GetMapping("/{username}/unassigned-trainers")
    @Operation(summary = "Get non-assigned active trainers for a trainee")
    public ResponseEntity<List<TrainerListItemDto>> getNotAssignedTrainers(
            @PathVariable String username) {
        return ResponseEntity.ok(trainerService.getUnassignedTrainers(username));
    }
}
