package com.sparta.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/DeleteRepo.feature",
        glue = "com.sparta.steps",
        plugin = {
                "pretty",
                "html:target/repository-report-delete.html",
                "json:target/repository-report-delete.json"
        },
        publish = true
)
public class DeleteRepoTestRunner {
}
