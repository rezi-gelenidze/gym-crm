package io.github.rezi_gelenidze.gym_crm.main_service.service;


import io.github.rezi_gelenidze.gym_crm.main_service.entity.TrainingType;
import io.github.rezi_gelenidze.gym_crm.main_service.repository.TrainingTypeRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }
}
