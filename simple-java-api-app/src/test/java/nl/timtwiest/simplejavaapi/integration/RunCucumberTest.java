package nl.timtwiest.simplejavaapi.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Slf4j
@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
public class RunCucumberTest {
    // empty
}
