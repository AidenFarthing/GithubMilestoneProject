package com.sparta.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/CreateRepo.feature",
        glue = "com.sparta.steps",
        plugin = {
                "pretty",
                "html:target/repository-report.html",
                "json:target/repository-report.json"
        },
        publish = true
)
public class CreateRepoTestRunner {
}
