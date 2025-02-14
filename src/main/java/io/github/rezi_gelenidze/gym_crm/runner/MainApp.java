package io.github.rezi_gelenidze.gym_crm.runner;

import io.github.rezi_gelenidze.gym_crm.entity.Trainee;
import io.github.rezi_gelenidze.gym_crm.facade.GymFacade;
import io.github.rezi_gelenidze.gym_crm.config.AppConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;


public class MainApp {
    public static void main(String[] args) {
        // Initialize Spring context using AppConfig to scan for beans
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get the GymFacade bean from the Spring context
        GymFacade gymFacade = context.getBean(GymFacade.class);

        // Create trainee
        Trainee trainee = gymFacade.createTrainee("John", "Doe", "1990-01-01", "Tbilisi, Georgia");

        System.out.println("Trainee created: " + trainee.getUser().getUsername());

        // Search for trainee
        Optional<Trainee> foundTrainee = gymFacade.getTrainee(trainee.getUser().getUserId());

        // Print the found trainee
        if (foundTrainee.isEmpty()) {
            System.out.println("Trainee not found");
        } else {
            System.out.println("Trainee found: " + foundTrainee.get().getUser().getUsername());
        }
    }
}
