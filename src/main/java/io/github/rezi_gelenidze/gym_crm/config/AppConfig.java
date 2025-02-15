package io.github.rezi_gelenidze.gym_crm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "io.github.rezi_gelenidze.gym_crm")
@PropertySource("classpath:application.properties")
public class AppConfig {
}
