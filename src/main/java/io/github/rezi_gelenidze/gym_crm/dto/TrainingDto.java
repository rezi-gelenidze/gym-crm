package io.github.rezi_gelenidze.gym_crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private String trainingTypeName;
    private LocalDate trainingDate;
    private Long trainingDuration;
}
