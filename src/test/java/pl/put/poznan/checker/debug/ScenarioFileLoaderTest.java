package pl.put.poznan.checker.debug;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScenarioFileLoaderTest {
    private static Scenario scenario, scenarioNoWhites, scenarioRandomWhites, ScenarioNoFirstDelim;

    @BeforeAll
    static void setSampleScenarios()
    {
        List<Scenario> list = createSampleScenarios();
        scenario = list.get(0);
        scenarioNoWhites = list.get(1);
        scenarioRandomWhites = list.get(2);
        ScenarioNoFirstDelim = list.get(3);
    }

    public static List<Scenario> createSampleScenarios()
    {
        List<Scenario> ret = new ArrayList<>();
        ScenarioFileLoader.delim = "-";
        ScenarioFileLoader.bIgnoreExcessWhitechars = true;
        ScenarioFileLoader.bStartWithDelim = true;
        try {
            File file = new File("testScenario.txt");
            FileWriter scenarioWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(scenarioWriter);

            scenarioWriter.write(   "Tytuł: Dodane książki\n" +
                    "Aktorzy: Bibliotekarz Inny KtośTam TamKtoś\n" +
                    "Aktor systemowy: System\n" +
                    "\n" +
                    "- Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +
                    "- Wyświetla się formularz.\n" +
                    "- Bibliotekarz podaje dane książki.\n" +
                    "- IF Bibliotekarz pragnie dodać egzemplarze książki\n" +
                    "-- Bibliotekarz wybiera opcję definiowania egzemplarzy\n" +
                    "-- System prezentuje zdefiniowane egzemplarze\n" +
                    "-- FOR EACH egzemplarz:\n" +
                    "--- Bibliotekarz wybiera opcję dodania egzemplarza\n" +
                    "--- System prosi o podanie danych egzemplarza\n" +
                    "--- Bibliotekarz podaje dane egzemplarza i zatwierdza.\n" +
                    "--- System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\n" +
                    "- Bibliotekarz zatwierdza dodanie książki.\n" +
                    "- System informuje o poprawnym dodaniu książki.\n");

            scenarioWriter.flush();
            ret.add(ScenarioFileLoader.loadScenario("testScenario.txt"));
            ScenarioFileLoader.bIgnoreExcessWhitechars = false;
            ret.add(ScenarioFileLoader.loadScenario("testScenario.txt"));
            ScenarioFileLoader.bIgnoreExcessWhitechars = true;
            scenarioWriter.close();
            writer.close();

            scenarioWriter = new FileWriter(file);
            writer = new BufferedWriter(scenarioWriter);
            scenarioWriter.write(   "\n" +"\n" +"Tytuł: Dodane książki\n" +
                    "Aktorzy: Bibliotekarz Inny KtośTam TamKtoś\n" +"\n" +"\n" +
                    "Aktor systemowy: System\n" +
                    "\n" +
                    "- Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +"\n" +
                    "- Wyświetla się formularz.\n" +"\n" +"\n" +
                    "- Bibliotekarz podaje dane książki.\n" +"\n" +
                    "- IF Bibliotekarz pragnie dodać egzemplarze książki\n" +
                    "-- Bibliotekarz wybiera opcję definiowania egzemplarzy\n" +"\n" +
                    "-- System prezentuje zdefiniowane egzemplarze\n" +
                    "-- FOR EACH egzemplarz:\n" +"\n" +"\n" +
                    "--- Bibliotekarz wybiera opcję dodania egzemplarza\n" +"\n" +
                    "--- System prosi o podanie danych egzemplarza\n" +"\n" +
                    "--- Bibliotekarz podaje dane egzemplarza i zatwierdza.\n" +"\n                 " +
                    "--- System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\n" +
                    "- Bibliotekarz zatwierdza dodanie książki.\n" +"\n" +"\n" +"\n" +"\n" +"\n" +
                    "- System informuje o poprawnym dodaniu książki.\n");
            scenarioWriter.flush();
            ret.add(ScenarioFileLoader.loadScenario("testScenario.txt"));
            scenarioWriter.close();
            writer.close();

            scenarioWriter = new FileWriter(file);
            writer = new BufferedWriter(scenarioWriter);
            scenarioWriter.write("Tytuł: Dodane książki\n" +
                    "Aktorzy: Bibliotekarz Inny KtośTam TamKtoś\n" +
                    "Aktor systemowy: System\n" +
                    "\n" +
                    " Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +
                    " Wyświetla się formularz.\n" +
                    " Bibliotekarz podaje dane książki.\n" +
                    " IF Bibliotekarz pragnie dodać egzemplarze książki\n" +
                    "- Bibliotekarz wybiera opcję definiowania egzemplarzy\n" +
                    "- System prezentuje zdefiniowane egzemplarze\n" +
                    "- FOR EACH egzemplarz:\n" +
                    "-- Bibliotekarz wybiera opcję dodania egzemplarza\n" +
                    "-- System prosi o podanie danych egzemplarza\n" +
                    "-- Bibliotekarz podaje dane egzemplarza i zatwierdza.\n" +
                    "-- System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\n" +
                    " Bibliotekarz zatwierdza dodanie książki.\n" +
                    " System informuje o poprawnym dodaniu książki.\n");
            scenarioWriter.flush();
            ScenarioFileLoader.bStartWithDelim = false;
            ret.add(ScenarioFileLoader.loadScenario("testScenario.txt"));
            scenarioWriter.close();
            writer.close();
            ScenarioFileLoader.bStartWithDelim = true;
            file.delete();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    @Test
    void scenarioProper()
    {
        checkLoadedScenario(scenario, false);
    }
    @Test
    void scenarioRandomWhites()
    {
        checkLoadedScenario(scenarioRandomWhites, false);
    }
    @Test
    void scenarioNoWhites()
    {
        checkLoadedScenario(scenarioNoWhites, true);
    }
    @Test
    void scenarioNoFirstDelim()
    {
        checkLoadedScenario(ScenarioNoFirstDelim, false);
    }
    void checkLoadedScenario(Scenario scenario, boolean bRespectWhites) {
        Assertions.assertNotNull(scenario);
        Assertions.assertEquals("Dodane książki", scenario.getName());
        Assertions.assertEquals("Bibliotekarz", scenario.getActor(0));
        Assertions.assertEquals("Inny", scenario.getActor(1));
        Assertions.assertEquals("KtośTam", scenario.getActor(2));
        Assertions.assertEquals("TamKtoś", scenario.getActor(3));
        Assertions.assertNull(scenario.getActor(4));
        Assertions.assertNotNull(scenario.getMain());
        SubScenario main = scenario.getMain();

        List<Step> steps = main.getSteps();
        Assertions.assertEquals(6,steps.size());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "Bibliotekarz wybiera opcje dodania nowej pozycji książkowej", steps.get(0).getText());
        Assertions.assertNull(steps.get(0).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "Wyświetla się formularz.", steps.get(1).getText());
        Assertions.assertNull(steps.get(1).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "Bibliotekarz podaje dane książki.", steps.get(2).getText());
        Assertions.assertNull(steps.get(2).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "IF Bibliotekarz pragnie dodać egzemplarze książki", steps.get(3).getText());
        Assertions.assertNotNull(steps.get(3).getChild());

        List<Step> subSteps = steps.get(3).getChild().getSteps();
        Assertions.assertEquals(3,subSteps.size());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "Bibliotekarz wybiera opcję definiowania egzemplarzy", subSteps.get(0).getText());
        Assertions.assertNull(subSteps.get(0).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "System prezentuje zdefiniowane egzemplarze", subSteps.get(1).getText());
        Assertions.assertNull(subSteps.get(1).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "FOR EACH egzemplarz:", subSteps.get(2).getText());
        Assertions.assertNotNull(subSteps.get(2).getChild());

        List<Step> subSubSteps = subSteps.get(2).getChild().getSteps();
        Assertions.assertEquals(4,subSubSteps.size());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "Bibliotekarz wybiera opcję dodania egzemplarza", subSubSteps.get(0).getText());
        Assertions.assertNull(subSubSteps.get(0).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "System prosi o podanie danych egzemplarza", subSubSteps.get(1).getText());
        Assertions.assertNull(subSubSteps.get(1).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "Bibliotekarz podaje dane egzemplarza i zatwierdza.", subSubSteps.get(2).getText());
        Assertions.assertNull(subSubSteps.get(2).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.", subSubSteps.get(3).getText());
        Assertions.assertNull(subSubSteps.get(3).getChild());

        Assertions.assertEquals((bRespectWhites ? " " : "") + "Bibliotekarz zatwierdza dodanie książki.", steps.get(4).getText());
        Assertions.assertNull(steps.get(4).getChild());
        Assertions.assertEquals((bRespectWhites ? " " : "") + "System informuje o poprawnym dodaniu książki.", steps.get(5).getText());
        Assertions.assertNull(steps.get(5).getChild());
    }
}