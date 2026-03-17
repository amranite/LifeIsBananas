package be.lifeisbananas;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * JUnit 5 runner that discovers and executes all Cucumber feature files.
 * Feature files must be placed in: src/test/resources/features/
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "be.lifeisbananas")
public class RunCucumberTest {
    // No body needed — @Suite drives everything
}
