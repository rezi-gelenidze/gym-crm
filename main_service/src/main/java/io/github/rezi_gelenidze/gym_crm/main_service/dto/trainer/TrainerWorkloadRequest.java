package io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TrainerWorkloadRequest {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private Long duration;
    private String actionType;
}
