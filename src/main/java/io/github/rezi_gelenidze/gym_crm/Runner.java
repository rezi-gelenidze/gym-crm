package io.github.rezi_gelenidze.gym_crm;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.facade.GymFacade;
import io.github.rezi_gelenidze.gym_crm.config.AppConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;


public class Runner {
    public static void main(String[] args) {
        // Initialize Spring context using AppConfig to scan for beans
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get the GymFacade bean from the Spring context
        GymFacade gymFacade = context.getBean(GymFacade.class);

        // Create trainee
        Trainee trainee = gymFacade.createTrainee("John", "Doe", "1990-01-01", "Tbilisi, Georgia");

        // Search for trainee
        gymFacade.getTrainee(trainee.getUserId());
    }
}
