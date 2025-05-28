package io.github.rezi_gelenidze.gym_crm.main_service.dto.training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingListItemDto {
    private String trainingName;

    private LocalDate trainingDate;

    private Long trainingTypeId;

    private Long trainingDuration;

    private String trainerUsername;
}
