package io.github.rezi_gelenidze.gym_crm.main_service.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "io.github.rezi_gelenidze.gym_crm.main_service.isolated.steps",
        plugin = {"pretty", "summary"}
)
public class CucumberTest {
}
