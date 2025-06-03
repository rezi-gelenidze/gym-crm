package io.github.rezi_gelenidze.gym_crm.workload_service.service;

import io.github.rezi_gelenidze.gym_crm.workload_service.dto.WorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.workload_service.dto.WorkloadResponse;
import io.github.rezi_gelenidze.gym_crm.workload_service.enums.WorkloadUpdateType;
import io.github.rezi_gelenidze.gym_crm.workload_service.model.Workload;
import io.github.rezi_gelenidze.gym_crm.workload_service.repository.WorkloadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkloadService {
    private final WorkloadRepository repository;

    public void recordWorkload(WorkloadRequest request) {
        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();

        // Get or create a workload, year, and month summaries
        Workload workload = repository.findByUsername(request.getUsername()).orElseGet(() -> {
            Workload w = new Workload();
            w.setUsername(request.getUsername());
            w.setFirstName(request.getFirstName());
            w.setLastName(request.getLastName());
            w.setActive(request.isActive());
            w.setYears(new ArrayList<>());
            return w;
        });

        Workload.YearSummary yearSummary = workload.getYears().stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElseGet(() -> {
                    Workload.YearSummary y = new Workload.YearSummary();
                    y.setYear(year);
                    y.setMonths(new ArrayList<>());
                    workload.getYears().add(y);
                    return y;
                });

        Workload.MonthSummary monthSummary = yearSummary.getMonths().stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseGet(() -> {
                    Workload.MonthSummary m = new Workload.MonthSummary();
                    m.setMonth(month);
                    m.setTrainingDurationMinutes(0);
                    yearSummary.getMonths().add(m);
                    return m;
                });

        // Update the training duration for the month (ADD or DELETE)
        int delta = request.getActionType() == WorkloadUpdateType.ADD
                ? request.getDuration().intValue()
                : -request.getDuration().intValue();

        monthSummary.setTrainingDurationMinutes(
                Math.max(0, monthSummary.getTrainingDurationMinutes() + delta)
        );

        repository.save(workload);
    }

    public Optional<WorkloadResponse> getWorkload(String username) {
        // Get the workload by username or propagate the 404 error
        Workload workload = repository.findByUsername(username).orElse(null);
        if (workload == null) return Optional.empty();

        // Serialize by years
        List<WorkloadResponse.YearlyWorkload> years = new ArrayList<>();

        for (Workload.YearSummary y : workload.getYears()) {
            // Serialize by months (only if training duration is greater than 0)
            List<WorkloadResponse.MonthlyWorkload> months = new ArrayList<>();

            for (Workload.MonthSummary m : y.getMonths()) {
                if (m.getTrainingDurationMinutes() > 0) {
                    months.add(new WorkloadResponse.MonthlyWorkload(
                            m.getMonth(), m.getTrainingDurationMinutes()
                    ));
                }
            }

            if (!months.isEmpty()) {
                years.add(new WorkloadResponse.YearlyWorkload(y.getYear(), months));
            }
        }

        return Optional.of(new WorkloadResponse(
                workload.getUsername(),
                workload.getFirstName(),
                workload.getLastName(),
                workload.isActive(),
                years
        ));
    }
}
