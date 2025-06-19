package io.github.rezi_gelenidze.gym_crm.workload_service.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/component",
        glue = "io.github.rezi_gelenidze.gym_crm.workload_service.component.steps",
        plugin = {"pretty", "summary"}
)
public class CucumberComponentTest {
}
