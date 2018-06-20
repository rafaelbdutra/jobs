package com.dutra.cron.jobs.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        glue = "com.dutra.cron.jobs.cucumber.steps"
)
public class CucumberTest {

}
