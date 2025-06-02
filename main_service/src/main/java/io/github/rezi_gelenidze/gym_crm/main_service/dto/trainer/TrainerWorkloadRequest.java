package io.github.rezi_gelenidze.gym_crm.main_service.dto.trainer;

import io.github.rezi_gelenidze.gym_crm.main_service.enums.WorkloadUpdateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkloadRequest implements Serializable {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private Long duration;
    private WorkloadUpdateType actionType;
}
