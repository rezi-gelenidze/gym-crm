package io.github.rezi_gelenidze.gym_crm.main_service.dto.trainee;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer.TrainerListItemDto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeProfileDto {

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String address;

    private boolean isActive;

    private List<TrainerListItemDto> trainers;
}
