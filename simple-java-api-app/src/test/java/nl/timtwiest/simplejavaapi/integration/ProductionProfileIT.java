package nl.timtwiest.simplejavaapi.integration;

import nl.timtwiest.simplejavaapi.app.App;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ActiveProfiles({"test", "prod"})
@SpringBootTest(
        classes = App.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:tc:postgresql:16.15-bookworm:///simplejavaapi",
                "spring.datasource.username=test",
                "spring.datasource.password=test",
                "management.server.port=0"
        })
class ProductionProfileIT {

    @Autowired
    private Environment environment;

    @LocalServerPort
    private int applicationPort;

    @LocalManagementPort
    private int managementPort;

    @Test
    void shouldApplySecureProductionDefaults() {
        assertEquals("validate", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        assertEquals("false", environment.getProperty("spring.flyway.baseline-on-migrate"));
        assertEquals("false", environment.getProperty("springdoc.api-docs.enabled"));
        assertEquals("false", environment.getProperty("springdoc.swagger-ui.enabled"));
        assertEquals("never", environment.getProperty("server.error.include-message"));
        assertEquals("never", environment.getProperty("server.error.include-binding-errors"));
        assertEquals("ecs", environment.getProperty("logging.structured.format.console"));
        assertNotEquals(applicationPort, managementPort);
    }

    @Test
    void shouldExposeOnlyHealthInformationOnSeparateManagementPort() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(200, get(client, managementPort, "/actuator/health").statusCode());
        assertEquals(200, get(client, managementPort, "/actuator/health/liveness").statusCode());
        assertEquals(200, get(client, managementPort, "/actuator/health/readiness").statusCode());
        assertEquals(404, get(client, applicationPort, "/actuator/health").statusCode());
        assertEquals(404, get(client, applicationPort, "/api/yaml").statusCode());
    }

    private static HttpResponse<String> get(HttpClient client, int port, String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
