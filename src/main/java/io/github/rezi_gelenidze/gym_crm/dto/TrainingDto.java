package io.github.rezi_gelenidze.gym_crm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {

    @NotNull(message = "Trainee ID is required")
    private Long traineeId;

    @NotNull(message = "Trainer ID is required")
    private Long trainerId;

    @NotBlank(message = "Training name is required")
    private String trainingName;

    @NotBlank(message = "Training type name is required")
    private String trainingTypeName;

    @NotNull(message = "Training date is required")
    private LocalDate trainingDate;

    @NotNull(message = "Training duration is required")
    @Positive(message = "Training duration must be a positive number")
    private Long trainingDuration;
}
