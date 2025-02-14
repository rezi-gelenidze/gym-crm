package io.github.rezi_gelenidze.gym_crm.storage;

import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Training;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class MemoryStorage {
    private final Map<Long, Trainer> trainerStorage = new ConcurrentHashMap<>();
    private final Map<Long, Trainee> traineeStorage = new ConcurrentHashMap<>();
    private final Map<String, Training> trainingStorage = new ConcurrentHashMap<>();
}
