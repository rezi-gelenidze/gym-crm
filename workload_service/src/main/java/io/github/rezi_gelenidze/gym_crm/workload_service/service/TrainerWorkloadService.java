package io.github.rezi_gelenidze.gym_crm.workload_service.service;

import io.github.rezi_gelenidze.gym_crm.workload_service.dto.TrainerWorkloadRequest;
import io.github.rezi_gelenidze.gym_crm.workload_service.dto.TrainerWorkloadResponse;
import io.github.rezi_gelenidze.gym_crm.workload_service.enums.WorkloadUpdateType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void recordWorkload(TrainerWorkloadRequest request) {
        // Use key for the specific trainer and date
        String key = String.format("trainer:%s:%d:%02d", request.getUsername(), request.getTrainingDate().getYear(), request.getTrainingDate().getMonthValue());
        long delta = request.getActionType().equals(WorkloadUpdateType.ADD) ? request.getDuration() : -request.getDuration();

        redisTemplate.opsForValue().increment(key, delta);

        // Store trainer info in a separate key
        String infoKey = String.format("trainer:%s:info", request.getUsername());
        redisTemplate.opsForHash().put(infoKey, "firstName", request.getFirstName());
        redisTemplate.opsForHash().put(infoKey, "lastName", request.getLastName());
        redisTemplate.opsForHash().put(infoKey, "isActive", request.isActive());
    }

    public TrainerWorkloadResponse getWorkload(String username) {
        // Assemble the key for the trainer's workload
        String infoKey = String.format("trainer:%s:info", username);
        Map<Object, Object> info = redisTemplate.opsForHash().entries(infoKey);

        // Extract trainer information and serialize
        String firstName = (String) info.getOrDefault("firstName", "Unknown");
        String lastName = (String) info.getOrDefault("lastName", "Unknown");
        boolean isActive = Boolean.parseBoolean(String.valueOf(info.getOrDefault("isActive", "false")));

        List<TrainerWorkloadResponse.YearlyWorkload> yearlyWorkloads = new ArrayList<>();

        // Stupid way, but it's a demo with in-memory storage :D
        for (int year = 2020; year <= LocalDate.now().getYear(); year++) {
            List<TrainerWorkloadResponse.MonthlyWorkload> months = new ArrayList<>();
            for (int month = 1; month <= 12; month++) {
                String key = String.format("trainer:%s:%d:%02d", username, year, month);

                Object val = redisTemplate.opsForValue().get(key);
                long duration = val instanceof Number ? ((Number) val).longValue() : 0L;

                if (duration > 0) {
                    months.add(new TrainerWorkloadResponse.MonthlyWorkload(month, duration));
                }
            }
            if (!months.isEmpty()) {
                yearlyWorkloads.add(new TrainerWorkloadResponse.YearlyWorkload(year, months));
            }
        }

        return new TrainerWorkloadResponse(username, firstName, lastName, isActive, yearlyWorkloads);
    }
}
