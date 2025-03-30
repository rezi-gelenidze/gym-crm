package io.github.rezi_gelenidze.gym_crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.*;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainerTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.service.TrainerService;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private TrainerService trainerService;
    @MockBean private TrainingService trainingService;
    @MockBean private UserService userService;

    private final String username = "trainer1";
    private final String password = "pass";

    @Test
    void createTrainer() throws Exception {
        TrainerCreateDto createDto = new TrainerCreateDto();
        Map<String, String> response = Map.of("username", username);

        when(trainerService.createTrainer(any())).thenReturn(response);

        mockMvc.perform(post("/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void getTrainerProfile() throws Exception {
        TrainerProfileDto profile = new TrainerProfileDto();

        when(trainerService.getTrainerProfile(username)).thenReturn(Optional.of(profile));
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);

        mockMvc.perform(get("/trainers/{username}", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk());
    }

    @Test
    void updateTrainerProfile() throws Exception {
        TrainerUpdateDto updateDto = new TrainerUpdateDto();
        TrainerProfileDto updated = new TrainerProfileDto();

        when(trainerService.updateTrainerProfile(updateDto)).thenReturn(Optional.of(updated));
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);

        mockMvc.perform(put("/trainers/{username}", username)
                        .header("X-Username", username)
                        .header("X-Password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void activateDeactivateTrainer() throws Exception {
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);
        doNothing().when(userService).updateActiveStatus(username, true);

        mockMvc.perform(patch("/trainers/{username}?isActive=true", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk());
    }

    @Test
    void getTrainerTrainings() throws Exception {
        TrainerTrainingListItemDto training = new TrainerTrainingListItemDto();
        training.setTraineeUsername("traineeX");

        when(trainingService.getTrainerTrainings(any(), any(), any(), any()))
                .thenReturn(List.of(training));
        doNothing().when(userService).authenticate(username, password);
        doNothing().when(userService).assertIdentity(username, username);

        mockMvc.perform(get("/trainers/{username}/trainings", username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].traineeUsername").value("traineeX"));
    }
}
