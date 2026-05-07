package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class StudentServiceSimulation extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://homeserver:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling/Load Test");

    // Scenario: List all students
    private ScenarioBuilder listStudentsScenario = scenario("List Students")
            .exec(http("GET /api/etudiants")
                    .get("/api/etudiants")
                    .check(status().is(200)));

    // Scenario: Create and retrieve a student
    private ScenarioBuilder createStudentScenario = scenario("Create and Retrieve Student")
            .exec(session -> {
                // Generate unique student data
                int randomId = (int) (System.currentTimeMillis() % 1000000);
                return session
                        .set("cin", "CIN" + randomId)
                        .set("nom", "Student " + randomId)
                        .set("dateNaissance", "2000-01-15")
                        .set("anneePremiereInscription", 2020);
            })
            .exec(http("GET /api/etudiants - Before Create")
                    .get("/api/etudiants")
                    .check(status().is(200)))
            .pause(Duration.ofMillis(100));

    // Scenario: Heavy load on list endpoint
    private ScenarioBuilder heavyListLoadScenario = scenario("Heavy List Load")
            .repeat(5).on(
                    exec(http("GET /api/etudiants - Iteration")
                            .get("/api/etudiants")
                            .check(status().is(200)))
                    .pause(Duration.ofMillis(500))
            );

    {
        setUp(
                // Ramp up: 50 users over 30 seconds
                listStudentsScenario.injectOpen(
                        rampUsers(50).during(Duration.ofSeconds(30))
                ),

                // Plateau: 20 users constant for 1 minute
                createStudentScenario.injectOpen(
                        atOnceUsers(20),
                        constantUsersPerSec(5).during(Duration.ofMinutes(1))
                ),

                // Spike: sudden 100 users
                heavyListLoadScenario.injectOpen(
                        atOnceUsers(100)
                )
        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(5000),
                        global().successfulRequests().percent().is(100.0),
                        global().responseTime().percentile(0.95).lt(2000)
                );
    }
}
