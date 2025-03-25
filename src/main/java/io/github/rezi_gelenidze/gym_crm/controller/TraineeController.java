package io.github.rezi_gelenidze.gym_crm.controller;

import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeProfileDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TraineeTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.service.TraineeService;
import io.github.rezi_gelenidze.gym_crm.service.TrainerService;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
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
            @RequestHeader("X-Username") String authUsername,
            @RequestHeader("X-Password") String authPassword,
            @PathVariable String username) {

        userService.authenticate(authUsername, authPassword);
        userService.assertIdentity(authUsername, username);

        Optional<TraineeProfileDto> trainee = traineeService.getTraineeProfile(username);
        return trainee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{username}")
    @Operation(summary = "Activate or Deactivate a trainee")
    public ResponseEntity<Void> activateDeactivateTrainee(
            @RequestHeader("X-Username") String authUsername,
            @RequestHeader("X-Password") String authPassword,
            @PathVariable String username,
            @RequestParam boolean isActive) {

        userService.authenticate(authUsername, authPassword);
        userService.assertIdentity(authUsername, username);

        userService.updateActiveStatus(username, isActive);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile")
    public ResponseEntity<Void> deleteTrainee(
            @RequestHeader("X-Username") String authUsername,
            @RequestHeader("X-Password") String authPassword,
            @PathVariable String username) {

        userService.authenticate(authUsername, authPassword);
        userService.assertIdentity(authUsername, username);

        traineeService.deleteTrainee(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainee's trainings list")
    public ResponseEntity<List<TraineeTrainingListItemDto>> getTraineeTrainings(
            @RequestHeader("X-Username") String authUsername,
            @RequestHeader("X-Password") String authPassword,
            @PathVariable String username,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) Long trainingTypeId) {

        userService.authenticate(authUsername, authPassword);
        userService.assertIdentity(authUsername, username);

        return ResponseEntity.ok(trainingService.getTraineeTrainings(username, from, to, trainerName, trainingTypeId));
    }

    @GetMapping("/{username}/unassigned-trainers")
    @Operation(summary = "Get non-assigned active trainers for a trainee")
    public ResponseEntity<List<TrainerListItemDto>> getNotAssignedTrainers(
            @RequestHeader("X-Username") String authUsername,
            @RequestHeader("X-Password") String authPassword,
            @PathVariable String username) {

        userService.authenticate(authUsername, authPassword);
        userService.assertIdentity(authUsername, username);

        return ResponseEntity.ok(trainerService.getUnassignedTrainers(username));
    }
}
