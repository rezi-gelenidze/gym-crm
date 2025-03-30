package io.github.rezi_gelenidze.gym_crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rezi_gelenidze.gym_crm.dto.auth.ChangePasswordRequestDto;
import io.github.rezi_gelenidze.gym_crm.dto.auth.CredentialsDto;
import io.github.rezi_gelenidze.gym_crm.exception.InvalidCredentialsException;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginSuccess() throws Exception {
        CredentialsDto credentials = new CredentialsDto("user1", "pass123");

        doNothing().when(userService).authenticate(any(CredentialsDto.class));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk());
    }

    @Test
    void loginFailure() throws Exception {
        CredentialsDto credentials = new CredentialsDto("user1", "wrongpass");

        doThrow(new InvalidCredentialsException()).when(userService).authenticate(any(CredentialsDto.class));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePasswordSuccess() throws Exception {
        ChangePasswordRequestDto changeRequest = new ChangePasswordRequestDto("user1", "oldpass", "newpass");

        doNothing().when(userService).authenticate(any(CredentialsDto.class));
        doNothing().when(userService).updatePassword("user1", "newpass");

        mockMvc.perform(put("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void changePasswordFailure() throws Exception {
        ChangePasswordRequestDto changeRequest = new ChangePasswordRequestDto("user1", "wrongold", "newpass");

        doThrow(new InvalidCredentialsException()).when(userService)
                .authenticate(new CredentialsDto("user1", "wrongold"));

        mockMvc.perform(put("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequest)))
                .andExpect(status().isUnauthorized());
    }
}
