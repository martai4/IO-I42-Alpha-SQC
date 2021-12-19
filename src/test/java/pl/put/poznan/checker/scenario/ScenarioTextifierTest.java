package pl.put.poznan.checker.scenario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.logic.ScenarioTextifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
class ScenarioTextifierTest {

    ScenarioTextifier visitor;


    @BeforeEach
    void createVisitor(){
        visitor = new ScenarioTextifier();
    }

    @Test
    void testVisitScenario() {
        Scenario scenario = new Scenario();
        scenario.setName("Title");
        scenario.setActors(Arrays.asList("Actor1","Actor2"));
        scenario.setSystemActor("System Actor");
        scenario.setMain(null);
        assertEquals(visitor, scenario.acceptVisitor(visitor));
        assertEquals("Tytuł: Title\n" +
                "Aktorzy: Actor1, Actor2\n" +
                "Aktor Systemowy: System Actor\n\n",visitor.getText());
    }

    @Test
    void testVisitSubScenario() {
        SubScenario subScenario = new SubScenario();
        subScenario.setSteps(Arrays.asList(new Step()));
        assertEquals(visitor,subScenario.acceptVisitor(visitor));
    }

    @Test
    void testVisitStep() {
        Step step = new Step();
        step.setText("Test");
        step.setChild(null);
        assertEquals(visitor,step.acceptVisitor(visitor));
        assertEquals("Test\n",visitor.getText());
    }

    @Test
    void testVisitFullScenario(){
        List<Step> steps = new ArrayList<>();
        Step step = new Step();
        step.setText("Step 1");
        step.setChild(null);
        steps.add(step);
        SubScenario child = new SubScenario();
        List<Step> subSteps = new ArrayList<>();
        step = new Step();
        step.setText("SubStep 1");
        step.setChild(null);
        subSteps.add(step);
        step = new Step();
        step.setText("SubStep 2");
        step.setChild(null);
        subSteps.add(step);
        step = new Step();
        child.setSteps(subSteps);
        step.setText("Step 2");
        step.setChild(child);
        steps.add(step);
        SubScenario main = new SubScenario();
        main.setSteps(steps);
        Scenario scenario = new Scenario();
        scenario.setName("Title");
        scenario.setActors(Arrays.asList("Actor1","Actor2"));
        scenario.setSystemActor("System Actor");
        scenario.setMain(main);
        assertEquals(visitor,scenario.acceptVisitor(visitor));
        assertEquals(
                "Tytuł: Title\n" +
                        "Aktorzy: Actor1, Actor2\n" +
                        "Aktor Systemowy: System Actor\n" +
                        "\n" +
                        "1.Step 1\n" +
                        "2.Step 2\n" +
                        "2.1.SubStep 1\n" +
                        "2.2.SubStep 2\n" ,visitor.getText());
    }
}