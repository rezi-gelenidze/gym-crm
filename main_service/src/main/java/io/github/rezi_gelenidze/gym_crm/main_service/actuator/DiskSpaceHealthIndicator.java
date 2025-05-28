package io.github.rezi_gelenidze.gym_crm.main_service.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        long freeSpace = new java.io.File("/").getFreeSpace();

        return (freeSpace > 100_000_000) // 100 MB free space as threshold
                ? Health.up().withDetail("freeSpace", freeSpace).build()
                : Health.down().withDetail("freeSpace", freeSpace).build();
    }
}
