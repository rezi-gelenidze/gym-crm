package io.github.rezi_gelenidze.gym_crm.workload_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "workloads")
public class Workload {

    @Id
    private String id;

    // Using username index and unique constraint for fast lookups and consistency
    @Indexed(unique = true)
    private String username;

    // I do not need firstName lastName index, but task requires it : D
    @Indexed
    private String firstName;

    @Indexed
    private String lastName;
    private boolean isActive;

    private List<YearSummary> years;

    // Nested list classes declared in private
    @Data
    public static class YearSummary {
        private int year;
        private List<MonthSummary> months;
    }

    @Data
    public static class MonthSummary {
        private int month;
        private long trainingDurationMinutes;
    }
}