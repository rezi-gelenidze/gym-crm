package io.github.rezi_gelenidze.gym_crm.main_service.actuator;

import io.github.rezi_gelenidze.gym_crm.main_service.service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class RegisteredUsersMetrics {

    private final MeterRegistry registry;
    private final UserService userService;

    @PostConstruct
    public void init() {
        registry.gauge("gym.active_users", userService, UserService::countUsers);
    }
}
