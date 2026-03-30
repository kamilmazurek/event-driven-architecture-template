package template.test;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

public abstract class AbstractActuatorIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnResponseFromActuatorEndpoint() {
        when()
                .get("/actuator")
                .then()
                .statusCode(200)
                .body(containsString("/actuator/health"));
    }

    @Test
    void shouldReturnStatusUpFromHealthEndpoint() {
        when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .body(containsString("{\"status\":\"UP\"}"));
    }

}
