package io.github.rezi_gelenidze.gym_crm.dto.trainer;

import io.github.rezi_gelenidze.gym_crm.dto.trainee.TraineeListItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileDto {

    private String firstName;

    private String lastName;

    private Long specializationId;

    private boolean isActive;

    private List<TraineeListItemDto> trainees;
}
