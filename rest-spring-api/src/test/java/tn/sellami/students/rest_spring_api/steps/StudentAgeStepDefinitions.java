package tn.sellami.students.rest_spring_api.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tn.sellami.students.rest_spring_api.entity.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Glue code that connects the Gherkin scenario to the Student age calculation logic.
public class StudentAgeStepDefinitions {

    // Scenario state shared across Given/When/Then steps.
    private LocalDate birthDate;
    private LocalDate referenceDate;
    private int calculatedAge;

    // Parse the input birth date and keep it for the age calculation step.
    @Given("a student with birth date {string}")
    public void aStudentWithBirthDate(String dateNaissance) {
        this.birthDate = LocalDate.parse(dateNaissance);
    }

    // Capture the reference date used to keep the test deterministic.
    @Given("today's date is {string}")
    public void todaysDateIs(String today) {
        this.referenceDate = LocalDate.parse(today);
    }

    // Create a Student instance and calculate the age from the captured dates.
    @When("I calculate the student's age")
    public void iCalculateTheStudentsAge() {
        Student student = new Student();
        student.setDateNaissance(this.birthDate);
        this.calculatedAge = student.ageAt(this.referenceDate);
    }

    // Verify that the computed age matches the expected value from the scenario.
    @Then("the returned age should be {int}")
    public void theReturnedAgeShouldBe(int expectedAge) {
        assertEquals(expectedAge, this.calculatedAge);
    }
}
