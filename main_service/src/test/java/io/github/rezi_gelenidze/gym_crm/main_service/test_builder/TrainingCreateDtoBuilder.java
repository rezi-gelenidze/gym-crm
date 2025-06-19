package io.github.rezi_gelenidze.gym_crm.main_service.test_builder;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.training.TrainingCreateDto;
import java.time.LocalDate;

public class TrainingCreateDtoBuilder {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private LocalDate trainingDate;
    private Long trainingDuration;


    public TrainingCreateDtoBuilder withTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
        return this;
    }

    public TrainingCreateDtoBuilder withTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
        return this;
    }

    public TrainingCreateDtoBuilder withTrainingName(String trainingName) {
        this.trainingName = trainingName;
        return this;
    }

    public TrainingCreateDtoBuilder withTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
        return this;
    }

    public TrainingCreateDtoBuilder withTrainingDuration(Long trainingDuration) {
        this.trainingDuration = trainingDuration;
        return this;
    }

    // methods for negative test cases (setting to null/empty)
    public TrainingCreateDtoBuilder withNullTraineeUsername() {
        this.traineeUsername = null;
        return this;
    }

    public TrainingCreateDtoBuilder withNullTrainerUsername() {
        this.trainerUsername = null;
        return this;
    }

    public TrainingCreateDtoBuilder withNullTrainingName() {
        this.trainingName = null;
        return this;
    }

    public TrainingCreateDtoBuilder withEmptyTrainingName() {
        this.trainingName = "";
        return this;
    }

    public TrainingCreateDtoBuilder withNullTrainingDate() {
        this.trainingDate = null;
        return this;
    }

    public TrainingCreateDtoBuilder withNullTrainingDuration() {
        this.trainingDuration = null;
        return this;
    }

    public TrainingCreateDto build() {
        return new TrainingCreateDto(
                traineeUsername,
                trainerUsername,
                trainingName,
                trainingDate,
                trainingDuration
        );
    }
}