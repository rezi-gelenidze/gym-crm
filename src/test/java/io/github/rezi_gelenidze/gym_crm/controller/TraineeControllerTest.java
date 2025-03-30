package io.github.rezi_gelenidze.gym_crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rezi_gelenidze.gym_crm.dto.trainee.*;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TraineeTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TraineeController.class)
class TraineeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private TraineeService traineeService;
    @MockBean private TrainerService trainerService;
    @MockBean private TrainingService trainingService;
    @MockBean private UserService userService;

    private final String username = "trainee1";
    private final String password = "pass";

    @Test
    void createTrainee() throws Exception {
        TraineeCreateDto createDto = new TraineeCreateDto();
        Map<String, String> response = Map.of("username", "trainee1");

        when(traineeService.createTrainee(any())).thenReturn(response);

        mockMvc.perform(post("/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("trainee1"));
    }

    @Test
    void getTraineeProfile() throws Exception {
        TraineeProfileDto profile = new TraineeProfileDto();

        when(traineeService.getTraineeProfile(username)).thenReturn(Optional.of(profile));
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);

        mockMvc.perform(get("/trainees/{username}", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk());
    }

    @Test
    void updateTraineeProfile() throws Exception {
        TraineeUpdateDto updateDto = new TraineeUpdateDto();
        TraineeProfileDto updated = new TraineeProfileDto();

        when(traineeService.updateTraineeProfile(updateDto)).thenReturn(Optional.of(updated));
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);

        mockMvc.perform(put("/trainees/{username}", username)
                        .header("X-Username", username)
                        .header("X-Password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTrainee() throws Exception {
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);
        doNothing().when(traineeService).deleteTrainee(username);

        mockMvc.perform(delete("/trainees/{username}", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk());
    }

    @Test
    void activateDeactivateTrainee() throws Exception {
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);
        doNothing().when(userService).updateActiveStatus(username, true);

        mockMvc.perform(patch("/trainees/{username}?isActive=true", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk());
    }

    @Test
    void getTraineeTrainings() throws Exception {
        TraineeTrainingListItemDto training = new TraineeTrainingListItemDto();
        training.setTrainerUsername("trainer1");

        when(trainingService.getTraineeTrainings(any(), any(), any(), any(), any()))
                .thenReturn(List.of(training));
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);

        mockMvc.perform(get("/trainees/{username}/trainings", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainerUsername").value("trainer1"));
    }

    @Test
    void getUnassignedTrainers() throws Exception {
        TrainerListItemDto trainer = new TrainerListItemDto();
        trainer.setUsername("trainer1");

        when(trainerService.getUnassignedTrainers(username)).thenReturn(List.of(trainer));
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);

        mockMvc.perform(get("/trainees/{username}/unassigned-trainers", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("trainer1"));
    }
}
