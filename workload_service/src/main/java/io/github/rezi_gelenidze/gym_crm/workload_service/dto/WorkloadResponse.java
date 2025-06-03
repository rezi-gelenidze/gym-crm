package io.github.rezi_gelenidze.gym_crm.workload_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorkloadResponse {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private List<YearlyWorkload> years;

    @Data
    @AllArgsConstructor
    public static class YearlyWorkload {
        private int year;
        private List<MonthlyWorkload> months;
    }

    @Data
    @AllArgsConstructor
    public static class MonthlyWorkload {
        private int month;
        private Long duration;
    }
}
