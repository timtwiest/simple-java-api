package nl.timtwiest.simplejavaapi.integration.common;

import io.cucumber.spring.CucumberContextConfiguration;
import nl.timtwiest.simplejavaapi.app.App;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringStepDefinition {
}
