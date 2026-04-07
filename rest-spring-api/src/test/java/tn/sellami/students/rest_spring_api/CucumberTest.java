package tn.sellami.students.rest_spring_api;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

// JUnit Platform entry point that discovers and runs the Cucumber feature files under src/test/resources/features.
// What's "CucumberTest"? It's a test suite that uses JUnit 5's @Suite to run Cucumber scenarios defined in Gherkin syntax.
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tn.sellami.students.rest_spring_api.steps")
public class CucumberTest {
}
