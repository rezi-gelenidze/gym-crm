package io.github.rezi_gelenidze.gym_crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rezi_gelenidze.gym_crm.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.exception.InvalidCredentialsException;
import io.github.rezi_gelenidze.gym_crm.service.TrainingTypeService;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingTypeController.class)
class TrainingTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingTypeService trainingTypeService;

    @MockBean
    private UserService userService;

    @Test
    void getAllTrainingTypesSuccess() throws Exception {
        TrainingType t1 = new TrainingType("Strength");
        TrainingType t2 = new TrainingType("Cardio");

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(List.of(t1, t2));

        doNothing().when(userService).authenticate("user1", "pass");

        mockMvc.perform(get("/training-types")
                        .header("X-Username", "user1")
                        .header("X-Password", "pass")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingTypeName").value("Strength"))
                .andExpect(jsonPath("$[1].trainingTypeName").value("Cardio"));
    }

    @Test
    void getAllTrainingTypesUnauthorized() throws Exception {
        doThrow(new InvalidCredentialsException())
                .when(userService).authenticate("user1", "wrong");

        mockMvc.perform(get("/training-types")
                        .header("X-Username", "user1")
                        .header("X-Password", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
