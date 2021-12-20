package pl.put.poznan.checker.scenario;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.debug.ScenarioFileLoader;
import pl.put.poznan.checker.debug.ScenarioFileLoaderTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioTextCheck {
    private static Scenario scenario;

    @BeforeAll
    static void setSampleScenario()
    {
        List<Scenario> list = ScenarioFileLoaderTest.createSampleScenarios();
        scenario = list.get(0);
    }
    @Test
    void howManyDecisions() {
        assertEquals(2,scenario.HowManyDecisions());
    }

    @Test
    void showActorsErrors() {
        List<String> lis=new ArrayList<>();
        lis.add("Wyświetla się formularz.");
        lis.add("FOR EACH egzemplarz:");
        assertEquals(lis,scenario.ShowActorsErrors());
    }
}