package pl.put.poznan.checker.scenario;

import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.debug.ScenarioFileLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioTextCheck {

    @Test
    void howManyDecisions() {
        ScenarioFileLoader t=new ScenarioFileLoader();
        Scenario test= null;
        try {
            test = t.loadScenario("testScenario.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(2,test.HowManyDecisions());
    }

    @Test
    void showActorsErrors() {
        ScenarioFileLoader t=new ScenarioFileLoader();
        Scenario test= null;
        try {
            test = t.loadScenario("testScenario.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> lis=new ArrayList<>();
        lis.add("WyĹ›wietla siÄ™ formularz.");
        lis.add("System prezentuje zdefiniowane egzemplarze");
        lis.add("FOR EACH egzemplarz:");
        assertEquals(lis,test.ShowActorsErrors());
    }
}