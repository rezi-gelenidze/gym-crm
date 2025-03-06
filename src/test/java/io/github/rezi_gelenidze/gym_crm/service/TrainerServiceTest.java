package io.github.rezi_gelenidze.gym_crm.service;

import io.github.rezi_gelenidze.gym_crm.dto.TrainerDto;
import io.github.rezi_gelenidze.gym_crm.dto.TrainerUpdateDto;
import io.github.rezi_gelenidze.gym_crm.entity.*;
import io.github.rezi_gelenidze.gym_crm.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        TrainerDto trainerDto = new TrainerDto("John", "Doe", "Strength Training");

        User mockUser = new User("John", "Doe", "john.doe", "hashedPassword");
        TrainingType trainingType = new TrainingType("Strength Training");
        Trainer trainer = new Trainer(mockUser, trainingType);

        when(userService.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userService.generatePassword()).thenReturn("hashedPassword");
        when(trainingTypeRepository.findByTrainingTypeName("Strength Training")).thenReturn(Optional.of(trainingType));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer createdTrainer = trainerService.createTrainer(trainerDto);

        assertNotNull(createdTrainer);
        assertEquals("john.doe", createdTrainer.getUser().getUsername());
        assertEquals("Strength Training", createdTrainer.getSpecialization().getTrainingTypeName());

        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void createTrainer_ShouldThrowException_WhenTrainingTypeNotFound() {
        TrainerDto trainerDto = new TrainerDto("John", "Doe", "Nonexistent Type");

        when(trainingTypeRepository.findByTrainingTypeName("Nonexistent Type")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> trainerService.createTrainer(trainerDto));

        assertEquals("Specialization not found: Nonexistent Type", exception.getMessage());
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainer() {
        String username = "john.doe";
        Trainer trainer = new Trainer(new User("John", "Doe", "john.doe", "password"), new TrainingType("Strength Training"));

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.getTrainerByUsername(username);

        assertTrue(foundTrainer.isPresent());
        assertEquals(username, foundTrainer.get().getUser().getUsername());

        verify(trainerRepository, times(1)).findByUsername(username);
    }

    @Test
    void getTrainerByUsername_ShouldReturnEmpty_WhenNotFound() {
        String username = "unknown.trainer";
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Trainer> foundTrainer = trainerService.getTrainerByUsername(username);

        assertFalse(foundTrainer.isPresent());

        verify(trainerRepository, times(1)).findByUsername(username);
    }

    @Test
    void updateTrainerProfile_ShouldUpdateSpecialization() {
        String username = "john.doe";
        Trainer trainer = new Trainer(new User("John", "Doe", "john.doe", "password"), new TrainingType("Old Specialization"));
        TrainerUpdateDto trainerUpdateDto = new TrainerUpdateDto("Strength Training");

        TrainingType newSpecialization = new TrainingType("Strength Training");

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByTrainingTypeName("Strength Training")).thenReturn(Optional.of(newSpecialization));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer updatedTrainer = trainerService.updateTrainerProfile(trainerUpdateDto, username);

        assertEquals("Strength Training", updatedTrainer.getSpecialization().getTrainingTypeName());

        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void updateTrainerProfile_ShouldThrowException_WhenTrainerNotFound() {
        String username = "unknown.trainer";
        TrainerUpdateDto trainerUpdateDto = new TrainerUpdateDto("Strength Training");

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> trainerService.updateTrainerProfile(trainerUpdateDto, username));

        assertEquals("Trainer not found with Username: unknown.trainer", exception.getMessage());
    }

    @Test
    void updateTrainerProfile_ShouldThrowException_WhenTrainingTypeNotFound() {
        String username = "john.doe";
        Trainer trainer = new Trainer(new User("John", "Doe", "john.doe", "password"), new TrainingType("Old Specialization"));
        TrainerUpdateDto trainerUpdateDto = new TrainerUpdateDto("Nonexistent Type");

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByTrainingTypeName("Nonexistent Type")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> trainerService.updateTrainerProfile(trainerUpdateDto, username));

        assertEquals("Specialization not found: Nonexistent Type", exception.getMessage());
    }
}
