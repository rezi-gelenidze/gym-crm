package io.github.rezi_gelenidze.gym_crm;

import io.github.rezi_gelenidze.gym_crm.config.AppConfig;
import io.github.rezi_gelenidze.gym_crm.config.JpaConfig;
import io.github.rezi_gelenidze.gym_crm.config.SecurityConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        // Manually load the Spring context and run the application
        // Triggering database initialization and seeding
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                AppConfig.class, JpaConfig.class, SecurityConfig.class
        );

        System.out.println("Spring Core App Started Successfully!");

        context.close();
    }
}
