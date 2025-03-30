package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerCreateDto;
import io.github.rezi_gelenidze.gym_crm.dto.trainer.TrainerProfileDto;
import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_ShouldCreateTrainerSuccessfully() {

        TrainerCreateDto trainerCreateDto = new TrainerCreateDto("John", "Doe", 1L);

        TrainingType trainingType = new TrainingType("Strength Training");

        when(userService.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userService.generateRawPassword()).thenReturn("rawPassword");
        when(userService.hashPassword("rawPassword")).thenReturn("hashedPassword");
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(trainingType));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, String> credentials = trainerService.createTrainer(trainerCreateDto);

        assertNotNull(credentials);
        assertEquals("john.doe", credentials.get("username"));
        assertEquals("rawPassword", credentials.get("password"));

        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void createTrainer_ShouldThrowException_WhenTrainingTypeNotFound() {
        TrainerCreateDto trainerCreateDto = new TrainerCreateDto("John", "Doe", -1L);

        when(trainingTypeRepository.findByTrainingTypeName("Nonexistent Type")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> trainerService.createTrainer(trainerCreateDto));
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainer() {
        String username = "john.doe";
        Trainer trainer = new Trainer(new User("John", "Doe", "john.doe", "password"), new TrainingType("Strength Training"));

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));

        Optional<TrainerProfileDto> foundTrainer = trainerService.getTrainerProfile(username);

        assertTrue(foundTrainer.isPresent());
        assertEquals("John", foundTrainer.get().getFirstName());
        assertEquals("Doe", foundTrainer.get().getLastName());

        verify(trainerRepository, times(1)).findByUsername(username);
    }

    @Test
    void getTrainerByUsername_ShouldReturnEmpty_WhenNotFound() {
        String username = "unknown.trainer";
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<TrainerProfileDto> foundTrainer = trainerService.getTrainerProfile(username);

        assertFalse(foundTrainer.isPresent());

        verify(trainerRepository, times(1)).findByUsername(username);
    }
}
