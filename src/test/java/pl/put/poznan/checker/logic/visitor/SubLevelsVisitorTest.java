package pl.put.poznan.checker.logic.visitor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.debug.ScenarioFileLoaderTest;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubLevelsVisitorTest {

    private static Scenario scenario;

    @BeforeAll
    static void setSampleScenarios() {
        List<Scenario> list = ScenarioFileLoaderTest.createSampleScenarios();
        scenario = list.get(0);
    }

    void testLevel(int actLvl, int maxLvl, Step origin, Step output) {
        actLvl += 1;
        assertEquals(origin.getText(), output.getText());
        if (actLvl == maxLvl) {
            assertNull(output.getChild());
        } else {
            if (origin.getChild() != null) {
                for (int j = 0; j < origin.getChild().getSteps().size(); j++) {
                    testLevel(actLvl, maxLvl, origin.getChild().getStep(j), output.getChild().getStep(j));
                }
            }
        }

    }

    void test(int maxLvl) throws Exception {
        SubLevelsVisitor test = new SubLevelsVisitor(maxLvl, "Test");
        scenario.acceptVisitor(test);
        Scenario output = test.getConverted();

        assertEquals("Test", output.getName());
        assertEquals(scenario.getActors(), output.getActors());
        assertEquals(scenario.getSystemActor(), output.getSystemActor());
        for (int i = 0; i < scenario.getMain().getSteps().size(); i++) {
            Step scenarioStep = scenario.getMain().getSteps().get(i);
            Step outputStep = output.getMain().getSteps().get(i);
            testLevel(0, maxLvl, scenarioStep, outputStep);
        }
    }

    @Test
    void getConverted() {
        try {
            test(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            test(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThrows(Exception.class, () -> {
            test(0);
        });
    }
}