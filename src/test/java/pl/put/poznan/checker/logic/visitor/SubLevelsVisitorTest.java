package pl.put.poznan.checker.logic.visitor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.debug.ScenarioFileLoaderTest;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void testWithMocks() {
        Scenario mockedScenario = mock(Scenario.class);
        SubScenario mockedSubScenario = mock(SubScenario.class);
        when(mockedScenario.getMain()).thenReturn(mockedSubScenario);
        when(mockedScenario.getSystemActor()).thenReturn("Mocked system actor");
        when(mockedScenario.getActors()).thenReturn(new ArrayList<String>() {{
            add("Mocked actor");
        }});

        ArrayList<Step> mockedSteps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Step mockedStep = mock(Step.class);
            when(mockedStep.getText()).thenReturn("Step " + String.valueOf(i));
            if (i % 2 != 0) {
                when(mockedStep.getChild()).thenReturn(null);
            } else {
                SubScenario innerMockedSubscenario = mock(SubScenario.class);
                Step newMockedStep = mock(Step.class);
                when(newMockedStep.getText()).thenReturn("Step substep " + String.valueOf(i));
                when(mockedStep.getChild()).thenReturn(innerMockedSubscenario);
                when(innerMockedSubscenario.getSteps()).thenReturn(new ArrayList<>() {{
                    add(newMockedStep);
                }});
            }
        }

        when(mockedSubScenario.getSteps()).thenReturn(mockedSteps);

        SubLevelsVisitor visitor1;
        SubLevelsVisitor visitor3;
        try {
            visitor1 = new SubLevelsVisitor(1, "Test lvl 1");
            visitor3 = new SubLevelsVisitor(3, "Test lvl 3");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        visitor1.visit(mockedScenario);
        visitor3.visit(mockedScenario);

        // porownanie nazw
        assertEquals("Test lvl 1", visitor1.getConverted().getName());
        assertEquals("Test lvl 3", visitor3.getConverted().getName());

        // porownanie poziomu 1
        SubScenario converted1 = visitor1.getConverted().getMain();
        for (int i = 0; i < mockedSteps.size(); i++) {
            assertEquals(mockedSteps.get(i).getText(), converted1.getSteps().get(i).getText());
            assertNull(converted1.getSteps().get(i).getChild());
        }

        // porownanie poziomu 3
        SubScenario converted3 = visitor3.getConverted().getMain();
        for (int i = 0; i < mockedSteps.size(); i++) {
            assertEquals(mockedSteps.get(i).getText(), converted3.getSteps().get(i).getText());
            assertEquals(mockedSteps.get(i).getChild(), converted3.getSteps().get(i).getChild());

            for (int j = 0; j < mockedSteps.get(i).getChild().getSteps().size(); j++) {
                Step ms = mockedSteps.get(i).getChild().getSteps().get(j);
                Step s = converted3.getSteps().get(i).getChild().getSteps().get(j);
                assertEquals(ms.getText(), s.getText());
                assertEquals(ms.getChild(), s.getChild());
            }

        }

    }
}