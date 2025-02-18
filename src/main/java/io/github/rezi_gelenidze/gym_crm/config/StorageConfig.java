package io.github.rezi_gelenidze.gym_crm.config;

import io.github.rezi_gelenidze.gym_crm.entity.Trainer;
import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.entity.Training;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    /*
        * Create a bean of type Map<KEY, ENTITY> for trainer, trainee and training
        * use ConcurrentHashMap as the implementation for memory storage
     */
    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }
}
