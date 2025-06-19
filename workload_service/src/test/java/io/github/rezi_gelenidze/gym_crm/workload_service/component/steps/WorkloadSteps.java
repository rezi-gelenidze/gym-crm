package io.github.rezi_gelenidze.gym_crm.workload_service.component.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.rezi_gelenidze.gym_crm.workload_service.controller.WorkloadController;
import io.github.rezi_gelenidze.gym_crm.workload_service.dto.WorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.workload_service.enums.WorkloadUpdateType;
import io.github.rezi_gelenidze.gym_crm.workload_service.messaging.WorkloadListener;
import io.github.rezi_gelenidze.gym_crm.workload_service.model.Workload;
import io.github.rezi_gelenidze.gym_crm.workload_service.repository.WorkloadRepository;
import io.github.rezi_gelenidze.gym_crm.workload_service.service.WorkloadService;
import io.github.rezi_gelenidze.gym_crm.workload_service.test_builder.WorkloadRequestBuilder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@CucumberContextConfiguration
public class WorkloadSteps {

    @Mock
    private WorkloadRepository workloadRepository;

    private WorkloadService workloadService;
    private WorkloadListener workloadListener;
    private WorkloadController workloadController;
    private MockMvc mockMvc;
    private WorkloadRequest workloadRequest;
    private MvcResult mvcResult;
    private ArgumentCaptor<Workload> workloadCaptor;
    private ObjectMapper objectMapper;
    private String trainerUsername;
    private Exception handledException;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        workloadCaptor = ArgumentCaptor.forClass(Workload.class);
        workloadService = new WorkloadService(workloadRepository);
        workloadListener = new WorkloadListener(workloadService, objectMapper);
        workloadController = new WorkloadController(workloadService);
        mockMvc = MockMvcBuilders.standaloneSetup(workloadController).build();
        trainerUsername = "trainer1";
        handledException = null;
    }

    @Given("the workload service is available")
    public void theWorkloadServiceIsAvailable() {
        assertNotNull(workloadService);
        assertNotNull(workloadController);
        assertNotNull(mockMvc);
    }

    @Given("a trainer already has workload data")
    public void aTrainerAlreadyHasWorkloadData() {
        Workload existingWorkload = new Workload();
        existingWorkload.setUsername(trainerUsername);
        existingWorkload.setFirstName("John");
        existingWorkload.setLastName("Doe");
        existingWorkload.setActive(true);

        List<Workload.YearSummary> years = new ArrayList<>();
        Workload.YearSummary year2024 = new Workload.YearSummary();
        year2024.setYear(2024);
        year2024.setMonths(new ArrayList<>());

        Workload.MonthSummary june = new Workload.MonthSummary();
        june.setMonth(6);
        june.setTrainingDurationMinutes(60);
        year2024.getMonths().add(june);

        years.add(year2024);
        existingWorkload.setYears(years);

        when(workloadRepository.findByUsername(trainerUsername)).thenReturn(Optional.of(existingWorkload));
    }

    @Given("a trainer has workload data across multiple months")
    public void aTrainerHasWorkloadDataAcrossMultipleMonths() {
        Workload workload = new Workload();
        workload.setUsername(trainerUsername);
        workload.setFirstName("John");
        workload.setLastName("Doe");
        workload.setActive(true);

        List<Workload.YearSummary> years = new ArrayList<>();

        Workload.YearSummary year2024 = new Workload.YearSummary();
        year2024.setYear(2024);
        year2024.setMonths(new ArrayList<>());

        Workload.MonthSummary may = new Workload.MonthSummary();
        may.setMonth(5);
        may.setTrainingDurationMinutes(45);
        year2024.getMonths().add(may);

        Workload.MonthSummary june = new Workload.MonthSummary();
        june.setMonth(6);
        june.setTrainingDurationMinutes(60);
        year2024.getMonths().add(june);

        years.add(year2024);

        Workload.YearSummary year2023 = new Workload.YearSummary();
        year2023.setYear(2023);
        year2023.setMonths(new ArrayList<>());

        Workload.MonthSummary december = new Workload.MonthSummary();
        december.setMonth(12);
        december.setTrainingDurationMinutes(30);
        year2023.getMonths().add(december);

        years.add(year2023);
        workload.setYears(years);

        when(workloadRepository.findByUsername(trainerUsername)).thenReturn(Optional.of(workload));
    }

    @When("a workload message is received with ADD action type")
    public void aWorkloadMessageIsReceivedWithADDActionType() throws Exception {
        workloadRequest = new WorkloadRequestBuilder()
                .withUsername(trainerUsername)
                .withFirstName("John")
                .withLastName("Doe")
                .withActive(true)
                .withTrainingDate(LocalDate.of(2024, 6, 15))
                .withDuration(30L)
                .withActionType(WorkloadUpdateType.ADD)
                .build();

        when(workloadRepository.save(any(Workload.class))).thenAnswer(invocation -> invocation.getArgument(0));
        String payload = objectMapper.writeValueAsString(workloadRequest);
        workloadListener.receive(payload);
    }

    @When("a workload message is received with DELETE action type")
    public void aWorkloadMessageIsReceivedWithDELETEActionType() throws Exception {
        workloadRequest = new WorkloadRequestBuilder()
                .withUsername(trainerUsername)
                .withFirstName("John")
                .withLastName("Doe")
                .withActive(true)
                .withTrainingDate(LocalDate.of(2024, 6, 15))
                .withDuration(30L)
                .withActionType(WorkloadUpdateType.DELETE)
                .build();

        when(workloadRepository.save(any(Workload.class))).thenAnswer(invocation -> invocation.getArgument(0));
        String payload = objectMapper.writeValueAsString(workloadRequest);
        workloadListener.receive(payload);
    }

    @When("I request the workload data for the trainer via the API")
    public void iRequestTheWorkloadDataForTheTrainerViaTheAPI() throws Exception {
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/workloads/" + trainerUsername))
                .andReturn();
    }

    @When("I request the workload data for a non-existent trainer")
    public void iRequestTheWorkloadDataForANonExistentTrainer() throws Exception {
        String nonExistentTrainer = "non-existent-trainer";
        when(workloadRepository.findByUsername(nonExistentTrainer)).thenReturn(Optional.empty());

        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/workloads/" + nonExistentTrainer))
                .andReturn();
    }

    @When("an invalid workload message is received")
    public void anInvalidWorkloadMessageIsReceived() {
        try {
            String invalidPayload = "{\"invalid\": \"json\"}";
            workloadListener.receive(invalidPayload);
        } catch (Exception e) {
            handledException = e;
        }
    }

    @Then("the workload data should be stored in MongoDB")
    public void theWorkloadDataShouldBeStoredInMongoDB() {
        verify(workloadRepository, times(1)).save(any(Workload.class));
    }

    @Then("the trainer should have the correct workload duration")
    public void theTrainerShouldHaveTheCorrectWorkloadDuration() {
        verify(workloadRepository).save(workloadCaptor.capture());
        Workload savedWorkload = workloadCaptor.getValue();

        List<Workload.YearSummary> years = savedWorkload.getYears();
        boolean found = false;

        for (Workload.YearSummary year : years) {
            if (year.getYear() == 2024) {
                for (Workload.MonthSummary month : year.getMonths()) {
                    if (month.getMonth() == 6) {
                        assertTrue(month.getTrainingDurationMinutes() >= 30);
                        found = true;
                        break;
                    }
                }
            }
        }

        assertTrue(found, "Workload data for June 2024 not found");
    }

    @Then("the workload data should be updated in MongoDB")
    public void theWorkloadDataShouldBeUpdatedInMongoDB() {
        verify(workloadRepository, times(1)).save(any(Workload.class));
    }

    @Then("the trainer workload should be increased accordingly")
    public void theTrainerWorkloadShouldBeIncreasedAccordingly() {
        verify(workloadRepository).save(workloadCaptor.capture());
        Workload savedWorkload = workloadCaptor.getValue();

        List<Workload.YearSummary> years = savedWorkload.getYears();

        for (Workload.YearSummary year : years) {
            if (year.getYear() == 2024) {
                for (Workload.MonthSummary month : year.getMonths()) {
                    if (month.getMonth() == 6) {
                        assertEquals(90, month.getTrainingDurationMinutes(),
                                "Expected 90 minutes (60 + 30) but got " + month.getTrainingDurationMinutes());
                        return;
                    }
                }
            }
        }

        fail("Workload data for June 2024 not found");
    }

    @Then("the trainer workload should be decreased accordingly")
    public void theTrainerWorkloadShouldBeDecreasedAccordingly() {
        verify(workloadRepository).save(workloadCaptor.capture());
        Workload savedWorkload = workloadCaptor.getValue();

        List<Workload.YearSummary> years = savedWorkload.getYears();

        for (Workload.YearSummary year : years) {
            if (year.getYear() == 2024) {
                for (Workload.MonthSummary month : year.getMonths()) {
                    if (month.getMonth() == 6) {
                        assertEquals(30, month.getTrainingDurationMinutes(),
                                "Expected 30 minutes (60 - 30) but got " + month.getTrainingDurationMinutes());
                        return;
                    }
                }
            }
        }

        fail("Workload data for June 2024 not found");
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertEquals(expectedStatusCode, mvcResult.getResponse().getStatus());
    }

    @Then("the response should contain the correct workload data")
    public void theResponseShouldContainTheCorrectWorkloadData() throws Exception {
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);

        assertEquals(trainerUsername, jsonNode.get("username").asText());
        assertEquals("John", jsonNode.get("firstName").asText());
        assertEquals("Doe", jsonNode.get("lastName").asText());

        assertTrue(jsonNode.has("years"));
        assertTrue(jsonNode.get("years").isArray());
        assertTrue(!jsonNode.get("years").isEmpty());

        boolean hasMonthData = false;
        JsonNode yearsArray = jsonNode.get("years");
        for (JsonNode year : yearsArray) {
            JsonNode monthsArray = year.get("months");
            if (!monthsArray.isEmpty()) {
                hasMonthData = true;
                break;
            }
        }
        assertTrue(hasMonthData, "No month data found in the response");
    }

    @Then("the service should handle the error gracefully")
    public void theServiceShouldHandleTheErrorGracefully() {
        assertNotNull(handledException, "Exception should have been thrown for invalid message");
    }
}