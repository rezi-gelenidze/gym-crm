package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.training.TraineeTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainerTrainingListItemDto;
import io.github.rezi_gelenidze.gym_crm.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.exception.UserNotFoundException;
import io.github.rezi_gelenidze.gym_crm.repository.TraineeRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_ShouldCreateAndReturnTraining() {
        TrainingCreateDto dto = new TrainingCreateDto(
                "john.doe", "trainer.doe", "Strength Training",
                LocalDate.of(2025, 3, 10), 60L
        );

        User traineeUser = new User("John", "Doe", "john.doe", "password123");
        User trainerUser = new User("Trainer", "Doe", "trainer.doe", "password123");

        TrainingType specialization = new TrainingType("Strength");
        Trainee trainee = new Trainee(traineeUser, LocalDate.of(2000, 1, 15), "123 Main St");
        Trainer trainer = new Trainer(trainerUser, specialization);

        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("trainer.doe")).thenReturn(Optional.of(trainer));
        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Training createdTraining = trainingService.createTraining(dto);

        assertNotNull(createdTraining);
        assertEquals("Strength Training", createdTraining.getTrainingName());
        assertEquals(specialization, createdTraining.getTrainingType());
        assertEquals(LocalDate.of(2025, 3, 10), createdTraining.getTrainingDate());
        assertEquals(60L, createdTraining.getTrainingDuration());

        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void createTraining_ShouldThrowException_WhenTraineeNotFound() {
        TrainingCreateDto dto = new TrainingCreateDto("john.doe", "trainer.doe", "Yoga", LocalDate.now(), 45L);

        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingService.createTraining(dto));
    }

    @Test
    void createTraining_ShouldThrowException_WhenTrainerNotFound() {
        TrainingCreateDto dto = new TrainingCreateDto("john.doe", "trainer.doe", "Yoga", LocalDate.now(), 45L);

        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(new Trainee()));
        when(trainerRepository.findByUsername("trainer.doe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingService.createTraining(dto));
    }

    @Test
    void getTraineeTrainings_ShouldReturnDtoList() {
        when(trainingRepository.findTraineeTrainings(anyString(), any(), any(), any(), any()))
                .thenReturn(List.of(new TraineeTrainingListItemDto()));

        List<TraineeTrainingListItemDto> trainings = trainingService.getTraineeTrainings(
                "john.doe", LocalDate.now(), LocalDate.now(), "trainer.doe", 1L);

        assertEquals(1, trainings.size());
        verify(trainingRepository, times(1)).findTraineeTrainings(anyString(), any(), any(), any(), any());
    }

    @Test
    void getTrainerTrainings_ShouldReturnDtoList() {
        when(trainingRepository.findTrainerTrainings(anyString(), any(), any(), any()))
                .thenReturn(List.of(new TrainerTrainingListItemDto()));

        List<TrainerTrainingListItemDto> trainings = trainingService.getTrainerTrainings(
                "trainer.doe", LocalDate.now(), LocalDate.now(), "john.doe");

        assertEquals(1, trainings.size());
        verify(trainingRepository, times(1)).findTrainerTrainings(anyString(), any(), any(), any());
    }
}
