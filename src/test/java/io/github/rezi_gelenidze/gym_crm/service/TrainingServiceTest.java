package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.TrainingDto;
import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_ShouldCreateAndReturnTraining() {
        TrainingDto trainingDto = new TrainingDto(
                1L, 2L, "Strength Training", "Strength", LocalDate.of(2025, 3, 10), 60L
        );

        User traineeUser = new User("John", "Doe", "john.doe", "password123");
        User trainerUser = new User("Trainer", "Doe", "trainer.doe", "password123");

        Trainee trainee = new Trainee(traineeUser, LocalDate.of(2000, 1, 15), "123 Main St");
        Trainer trainer = new Trainer(trainerUser, new TrainingType("Strength"));
        TrainingType trainingType = new TrainingType("Strength");

        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findById(2L)).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByTrainingTypeName("Strength")).thenReturn(Optional.of(trainingType));
        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Training createdTraining = trainingService.createTraining(trainingDto);

        assertNotNull(createdTraining);
        assertEquals("Strength Training", createdTraining.getTrainingName());
        assertEquals(trainingType, createdTraining.getTrainingType());
        assertEquals(LocalDate.of(2025, 3, 10), createdTraining.getTrainingDate());
        assertEquals(60L, createdTraining.getTrainingDuration());

        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void createTraining_ShouldThrowException_WhenTraineeNotFound() {
        TrainingDto trainingDto = new TrainingDto(1L, 2L, "Yoga", "Yoga", LocalDate.now(), 45L);

        when(traineeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(trainingDto));
        assertEquals("Trainee not found: 1", exception.getMessage());
    }

    @Test
    void createTraining_ShouldThrowException_WhenTrainerNotFound() {
        TrainingDto trainingDto = new TrainingDto(1L, 2L, "Yoga", "Yoga", LocalDate.now(), 45L);

        when(traineeRepository.findById(1L)).thenReturn(Optional.of(new Trainee()));
        when(trainerRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(trainingDto));
        assertEquals("Trainer not found: 2", exception.getMessage());
    }

    @Test
    void createTraining_ShouldThrowException_WhenTrainingTypeNotFound() {
        TrainingDto trainingDto = new TrainingDto(1L, 2L, "Yoga", "Yoga", LocalDate.now(), 45L);

        when(traineeRepository.findById(1L)).thenReturn(Optional.of(new Trainee()));
        when(trainerRepository.findById(2L)).thenReturn(Optional.of(new Trainer()));
        when(trainingTypeRepository.findByTrainingTypeName("Yoga")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> trainingService.createTraining(trainingDto));
        assertEquals("Training Type not found: Yoga", exception.getMessage());
    }

    @Test
    void getTraineeTrainings_ShouldReturnTrainingList() {
        when(trainingRepository.findTraineeTrainings(anyString(), any(), any(), any(), any())).thenReturn(List.of(new Training()));

        List<Training> trainings = trainingService.getTraineeTrainings("john.doe", LocalDate.now(), LocalDate.now(), "Trainer Name", "Strength");

        assertEquals(1, trainings.size());
        verify(trainingRepository, times(1)).findTraineeTrainings(anyString(), any(), any(), any(), any());
    }

    @Test
    void getTrainerTrainings_ShouldReturnTrainingList() {
        when(trainingRepository.findTrainerTrainings(anyString(), any(), any(), any())).thenReturn(List.of(new Training()));

        List<Training> trainings = trainingService.getTrainerTrainings("trainer.doe", LocalDate.now(), LocalDate.now(), "Trainee Name");

        assertEquals(1, trainings.size());
        verify(trainingRepository, times(1)).findTrainerTrainings(anyString(), any(), any(), any());
    }
}
