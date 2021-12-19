package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.debug.ScenarioFileLoaderTest;
import pl.put.poznan.checker.scenario.Scenario;

import java.util.List;

class ScenarioQualityCheckerTest {
    private static Scenario scenario, scenarioRandomWhites, ScenarioNoFirstDelim, scenarioNoWhites;

    @BeforeAll
    static void setSampleScenarios()
    {
        List<Scenario> list = ScenarioFileLoaderTest.createSampleScenarios();
        scenario = list.get(0);
        scenarioRandomWhites = list.get(1);
        ScenarioNoFirstDelim = list.get(2);
        scenarioNoWhites = list.get(3);
    }

    @Test
    void scenarioProper()
    {
        checkLoadedScenario(scenario);
    }
    @Test
    void scenarioRandomWhites()
    {
        checkLoadedScenario(scenarioRandomWhites);
    }
    @Test
    void scenarioNoWhites()
    {
        checkLoadedScenario(scenarioNoWhites);
    }
    @Test
    void scenarioNoFirstDelim()
    {
        checkLoadedScenario(ScenarioNoFirstDelim);
    }

    @Test
    void scenarioEmpty()
    {
        ScenarioQualityChecker checker = new ScenarioQualityChecker();
        Assertions.assertEquals(0, checker.getScenarioSize(new Scenario()));
    }

    void checkLoadedScenario(Scenario scenario) {
        ScenarioQualityChecker checker = new ScenarioQualityChecker();
        Assertions.assertEquals(13,checker.getScenarioSize(scenario));
    }
}