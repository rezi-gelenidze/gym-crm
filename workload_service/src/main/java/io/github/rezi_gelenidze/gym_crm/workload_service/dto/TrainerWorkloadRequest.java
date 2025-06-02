package io.github.rezi_gelenidze.gym_crm.workload_service.dto;

import io.github.rezi_gelenidze.gym_crm.workload_service.enums.WorkloadUpdateType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TrainerWorkloadRequest {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private Long duration;
    private WorkloadUpdateType actionType;
}
