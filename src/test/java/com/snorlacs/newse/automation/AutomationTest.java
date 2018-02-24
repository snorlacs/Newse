package com.snorlacs.newse.automation;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/automation")

@WebAppConfiguration
public class AutomationTest {
}
