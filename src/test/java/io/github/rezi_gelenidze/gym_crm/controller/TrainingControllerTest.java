package io.github.rezi_gelenidze.gym_crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.entity.Training;
import io.github.rezi_gelenidze.gym_crm.exception.InvalidCredentialsException;
import io.github.rezi_gelenidze.gym_crm.service.TrainingService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTrainingSuccess() throws Exception {
        TrainingCreateDto dto = new TrainingCreateDto();
        dto.setTrainerUsername("trainer1");

        doNothing().when(userService).authenticate("trainer1", "pass");
        doNothing().when(userService).assertIdentity("trainer1", "trainer1");
        doReturn(
                new Training()
        ).when(trainingService).createTraining(dto);

        mockMvc.perform(post("/trainings")
                        .header("X-Username", "trainer1")
                        .header("X-Password", "pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createTrainingUnauthorized() throws Exception {
        TrainingCreateDto dto = new TrainingCreateDto();
        dto.setTrainerUsername("trainer1");

        doThrow(new InvalidCredentialsException()).when(userService).authenticate("trainer1", "wrong");

        mockMvc.perform(post("/trainings")
                        .header("X-Username", "trainer1")
                        .header("X-Password", "wrong")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTrainingForbidden() throws Exception {
        TrainingCreateDto dto = new TrainingCreateDto();
        dto.setTrainerUsername("trainer1");

        doNothing().when(userService).authenticate("userX", "pass");
        doThrow(new InvalidCredentialsException()).when(userService).assertIdentity("userX", "trainer1");

        mockMvc.perform(post("/trainings")
                        .header("X-Username", "userX")
                        .header("X-Password", "pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}
