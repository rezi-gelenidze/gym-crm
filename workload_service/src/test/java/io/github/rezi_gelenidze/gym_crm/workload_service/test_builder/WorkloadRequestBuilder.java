package io.github.rezi_gelenidze.gym_crm.workload_service.test_builder;

import io.github.rezi_gelenidze.gym_crm.workload_service.dto.WorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.workload_service.enums.WorkloadUpdateType;

import java.time.LocalDate;

public class WorkloadRequestBuilder {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private Long duration;
    private WorkloadUpdateType actionType;

    public WorkloadRequestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public WorkloadRequestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public WorkloadRequestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public WorkloadRequestBuilder withActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public WorkloadRequestBuilder withTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
        return this;
    }

    public WorkloadRequestBuilder withDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public WorkloadRequestBuilder withActionType(WorkloadUpdateType actionType) {
        this.actionType = actionType;
        return this;
    }

    // Methods for negative test cases (setting to null)
    public WorkloadRequestBuilder withNullUsername() {
        this.username = null;
        return this;
    }

    public WorkloadRequestBuilder withNullFirstName() {
        this.firstName = null;
        return this;
    }

    public WorkloadRequestBuilder withNullLastName() {
        this.lastName = null;
        return this;
    }

    public WorkloadRequestBuilder withNullTrainingDate() {
        this.trainingDate = null;
        return this;
    }

    public WorkloadRequestBuilder withNullDuration() {
        this.duration = null;
        return this;
    }

    public WorkloadRequestBuilder withNullActionType() {
        this.actionType = null;
        return this;
    }

    public WorkloadRequest build() {
        return new WorkloadRequest(
                username,
                firstName,
                lastName,
                isActive,
                trainingDate,
                duration,
                actionType
        );
    }
}