package io.github.rezi_gelenidze.gym_crm.workload_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WorkloadServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkloadServiceApplication.class, args);
    }
}
