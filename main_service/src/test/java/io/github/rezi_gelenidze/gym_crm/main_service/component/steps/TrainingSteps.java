package io.github.rezi_gelenidze.gym_crm.main_service.component.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.rezi_gelenidze.gym_crm.main_service.controller.TrainingController;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TrainingCreateDto;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.Training;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.main_service.entity.User;
import io.github.rezi_gelenidze.gym_crm.main_service.enums.WorkloadUpdateType;
import io.github.rezi_gelenidze.gym_crm.main_service.exception.GlobalExceptionHandler;
import io.github.rezi_gelenidze.gym_crm.main_service.repository.TraineeRepository;
import io.github.rezi_gelenidze.gym_crm.main_service.repository.TrainerRepository;
import io.github.rezi_gelenidze.gym_crm.main_service.repository.TrainingRepository;
import io.github.rezi_gelenidze.gym_crm.main_service.service.TrainingService;
import io.github.rezi_gelenidze.gym_crm.main_service.service.WorkloadService;
import io.github.rezi_gelenidze.gym_crm.main_service.test_builder.TrainingCreateDtoBuilder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
@CucumberContextConfiguration
public class TrainingSteps {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private WorkloadService workloadService;

    @Mock
    private HttpServletRequest request;

    private MockMvc mockMvc;
    private TrainingController trainingController;
    private TrainingCreateDto trainingCreateDto;
    private MvcResult mvcResult;
    private ArgumentCaptor<Training> trainingCaptor;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize ObjectMapper properly
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configure real service with mocked repositories
        TrainingService realTrainingService = new TrainingService(
                trainerRepository,
                traineeRepository,
                trainingRepository,
                workloadService
        );

        trainingController = new TrainingController(realTrainingService);

        // Add the GlobalExceptionHandler to properly handle validation errors
        mockMvc = MockMvcBuilders
                .standaloneSetup(trainingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        trainingCaptor = ArgumentCaptor.forClass(Training.class);

        // Mock Authentication
        ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
        when(attributes.getRequest()).thenReturn(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Given("the training service is available")
    public void theTrainingServiceIsAvailable() {
        assertNotNull(trainingController);
        assertNotNull(mockMvc);
    }

    @When("I create a training with valid information")
    public void iCreateATrainingWithValidInformation() throws Exception {
        // Setup training data
        trainingCreateDto = new TrainingCreateDtoBuilder()
                .withTraineeUsername("trainee1")
                .withTrainerUsername("trainer1")
                .withTrainingName("Valid Training")
                .withTrainingDate(LocalDate.now())
                .withTrainingDuration(60L)
                .build();

        // Mock trainee
        User traineeUser = new User();
        traineeUser.setUsername("trainee1");
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));

        // Mock trainer with specialization
        User trainerUser = new User();
        trainerUser.setUsername("trainer1");
        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Cardio");
        trainer.setSpecialization(specialization);
        when(trainerRepository.findByUsername("trainer1")).thenReturn(Optional.of(trainer));

        // Mock repository save
        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(1L);
            return training;
        });

        // Execute request
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/trainings")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andReturn();
    }

    @When("I create a training with missing {string}")
    public void iCreateATrainingWithMissing(String field) throws Exception {
        TrainingCreateDtoBuilder builder = new TrainingCreateDtoBuilder()
                .withTraineeUsername("trainee1")
                .withTrainerUsername("trainer1")
                .withTrainingName("Test Training")
                .withTrainingDate(LocalDate.now())
                .withTrainingDuration(60L);

        // Set the specific field to null based on the field name
        switch (field) {
            case "traineeUsername" -> builder.withNullTraineeUsername();
            case "trainerUsername" -> builder.withNullTrainerUsername();
            case "trainingName" -> builder.withNullTrainingName();
            case "trainingDate" -> builder.withNullTrainingDate();
            case "trainingDuration" -> builder.withNullTrainingDuration();
        }

        trainingCreateDto = builder.build();

        // Execute request
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/trainings")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andReturn();
    }

    @When("I create a training with a non-existent trainee")
    public void iCreateATrainingWithANonExistentTrainee() throws Exception {
        trainingCreateDto = new TrainingCreateDtoBuilder()
                .withTraineeUsername("non-existent-trainee")
                .withTrainerUsername("trainer1")
                .withTrainingName("Test Training")
                .withTrainingDate(LocalDate.now())
                .withTrainingDuration(60L)
                .build();

        // Mock non-existent trainee
        when(traineeRepository.findByUsername("non-existent-trainee")).thenReturn(Optional.empty());

        // Mock trainer with specialization
        User trainerUser = new User();
        trainerUser.setUsername("trainer1");
        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Cardio");
        trainer.setSpecialization(specialization);
        when(trainerRepository.findByUsername("trainer1")).thenReturn(Optional.of(trainer));

        // Execute request
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/trainings")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andReturn();
    }

    @When("I create a training with a non-existent trainer")
    public void iCreateATrainingWithANonExistentTrainer() throws Exception {
        trainingCreateDto = new TrainingCreateDtoBuilder()
                .withTraineeUsername("trainee1")
                .withTrainerUsername("non-existent-trainer")
                .withTrainingName("Test Training")
                .withTrainingDate(LocalDate.now())
                .withTrainingDuration(60L)
                .build();

        // Mock trainee
        User traineeUser = new User();
        traineeUser.setUsername("trainee1");
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));

        // Mock non-existent trainer
        when(trainerRepository.findByUsername("non-existent-trainer")).thenReturn(Optional.empty());

        // Execute request
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/trainings")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andReturn();
    }

    @When("I create a training with an empty training name")
    public void iCreateATrainingWithAnEmptyTrainingName() throws Exception {
        trainingCreateDto = new TrainingCreateDtoBuilder()
                .withTraineeUsername("trainee1")
                .withTrainerUsername("trainer1")
                .withTrainingName("")
                .withTrainingDate(LocalDate.now())
                .withTrainingDuration(60L)
                .build();

        // Execute request
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/trainings")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andReturn();
    }

    @When("I create a training with a negative duration")
    public void iCreateATrainingWithANegativeDuration() throws Exception {
        trainingCreateDto = new TrainingCreateDtoBuilder()
                .withTraineeUsername("trainee1")
                .withTrainerUsername("trainer1")
                .withTrainingName("Test Training")
                .withTrainingDate(LocalDate.now())
                .withTrainingDuration(-60L)
                .build();

        // Execute request
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/trainings")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andReturn();
    }

    @When("I create a training with minimum required information")
    public void iCreateATrainingWithMinimumRequiredInformation() throws Exception {
        // Setup training data with minimum required fields
        trainingCreateDto = new TrainingCreateDtoBuilder()
                .withTraineeUsername("trainee1")
                .withTrainerUsername("trainer1")
                .withTrainingName("Minimum Training")
                .withTrainingDate(LocalDate.now())
                .withTrainingDuration(30L)
                .build();

        // Mock trainee
        User traineeUser = new User();
        traineeUser.setUsername("trainee1");
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));

        // Mock trainer with specialization
        User trainerUser = new User();
        trainerUser.setUsername("trainer1");
        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Cardio");
        trainer.setSpecialization(specialization);
        when(trainerRepository.findByUsername("trainer1")).thenReturn(Optional.of(trainer));

        // Mock repository save
        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(1L);
            return training;
        });

        // Execute request
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/trainings")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(trainingCreateDto)))
                .andReturn();
    }

    @Then("the training should be created successfully")
    public void theTrainingShouldBeCreatedSuccessfully() {
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Then("the workload service should be notified with ADD type")
    public void theWorkloadServiceShouldBeNotifiedWithADDType() {
        verify(workloadService, times(1)).notifyWorkloadService(any(Training.class), any(WorkloadUpdateType.class));
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertEquals(expectedStatusCode, mvcResult.getResponse().getStatus());
    }

    @Then("the error code should be {string}")
    public void theErrorCodeShouldBe(String expectedErrorCode) throws Exception {
        String responseContent = mvcResult.getResponse().getContentAsString();

        // Check if response has content
        if (responseContent == null || responseContent.isEmpty()) {
            fail("Response body is empty. Cannot verify error code.");
        }

        try {
            // Use JsonNode to parse the JSON without needing a specific class structure
            JsonNode jsonNode = objectMapper.readTree(responseContent);
            JsonNode errorNode = jsonNode.get("error");

            if (errorNode == null) {
                fail("Error field not found in response: " + responseContent);
            }

            String actualErrorCode = errorNode.asText();
            assertEquals(expectedErrorCode, actualErrorCode,
                    "Expected error code '" + expectedErrorCode + "' but got '" +
                            actualErrorCode + "'. Full response: " + responseContent);

        } catch (Exception e) {
            fail("Failed to parse error response: " + e.getMessage() +
                    ". Response content: " + responseContent);
        }
    }
}