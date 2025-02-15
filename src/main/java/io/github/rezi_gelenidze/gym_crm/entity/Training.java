package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.Duration;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    // Relationship keys
    private Long traineeId;
    private Long trainerId;

    // Define own attributes
    private String trainingName;
    private TrainingType trainingTypeName;
    private String trainingDate;
    private Duration trainingDuration;
}
