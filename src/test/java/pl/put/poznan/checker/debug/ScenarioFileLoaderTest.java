package pl.put.poznan.checker.debug;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.logic.ScenarioFormat;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

import java.io.*;
import java.util.List;

public class ScenarioFileLoaderTest
{
    @Test
    void scenarioProper() throws IOException
    {
        ScenarioFormat.bEnumerate = false;
        ScenarioFormat.delim = "-";
        ScenarioFormat.bIgnoreExcessWhitechars = true;
        ScenarioFormat.bStartWithDelim = true;

        //StringReader stanowi klasę zastępczą (mock) dla FileReader, który powinien tutaj zostać wykorzystany
        StringReader strReader = new StringReader("Tytuł: Dodane książki\n" +
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
        Scenario scenario = ScenarioFileLoader.loadScenario(new BufferedReader(strReader));
        checkLoadedScenario(scenario, false);
    }

    @Test
    void scenarioProperEnumerate() throws IOException
    {
        ScenarioFormat.bEnumerate = true;
        ScenarioFormat.delim = "-";
        ScenarioFormat.bIgnoreExcessWhitechars = true;
        ScenarioFormat.bStartWithDelim = true;

        //StringReader stanowi klasę zastępczą (mock) dla FileReader, który powinien tutaj zostać wykorzystany
        StringReader strReader = new StringReader("Tytuł: Dodane książki\n" +
                "Aktorzy: Bibliotekarz Inny KtośTam TamKtoś\n" +
                "Aktor systemowy: System\n" +
                "\n" +
                "1. Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +
                "2. Wyświetla się formularz.\n" +
                "3. Bibliotekarz podaje dane książki.\n" +
                "4. IF Bibliotekarz pragnie dodać egzemplarze książki\n" +
                "4.1. Bibliotekarz wybiera opcję definiowania egzemplarzy\n" +
                "4.2. System prezentuje zdefiniowane egzemplarze\n" +
                "4.3. FOR EACH egzemplarz:\n" +
                "4.3.1. Bibliotekarz wybiera opcję dodania egzemplarza\n" +
                "4.3.2. System prosi o podanie danych egzemplarza\n" +
                "4.3.3. Bibliotekarz podaje dane egzemplarza i zatwierdza.\n" +
                "4.3.4. System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\n" +
                "5. Bibliotekarz zatwierdza dodanie książki.\n" +
                "6. System informuje o poprawnym dodaniu książki.\n");
        Scenario scenario = ScenarioFileLoader.loadScenario(new BufferedReader(strReader));
        checkLoadedScenario(scenario, false);
    }

    @Test
    void scenarioRandomWhites() throws IOException
    {
        ScenarioFormat.bEnumerate = false;
        ScenarioFormat.delim = "-";
        ScenarioFormat.bIgnoreExcessWhitechars = true;
        ScenarioFormat.bStartWithDelim = true;

        //StringReader stanowi klasę zastępczą (mock) dla FileReader, który powinien tutaj zostać wykorzystany
        StringReader strReader = new StringReader("\n" +"\n" +"Tytuł: Dodane książki\n" +
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
        Scenario scenario = ScenarioFileLoader.loadScenario(new BufferedReader(strReader));
        checkLoadedScenario(scenario, false);
    }

    @Test
    void scenarioNoWhites() throws IOException
    {
        ScenarioFormat.bEnumerate = false;
        ScenarioFormat.delim = "-";
        ScenarioFormat.bIgnoreExcessWhitechars = false;
        ScenarioFormat.bStartWithDelim = true;

        //StringReader stanowi klasę zastępczą (mock) dla FileReader, który powinien tutaj zostać wykorzystany
        StringReader strReader = new StringReader("Tytuł: Dodane książki\n" +
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
        Scenario scenario = ScenarioFileLoader.loadScenario(new BufferedReader(strReader));
        checkLoadedScenario(scenario, true);
    }

    @Test
    void scenarioNoFirstDelim() throws IOException
    {
        ScenarioFormat.bEnumerate = false;
        ScenarioFormat.delim = "-";
        ScenarioFormat.bIgnoreExcessWhitechars = true;
        ScenarioFormat.bStartWithDelim = false;

        //StringReader stanowi klasę zastępczą (mock) dla FileReader, który powinien tutaj zostać wykorzystany
        StringReader strReader = new StringReader("Tytuł: Dodane książki\n" +
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
        Scenario scenario = ScenarioFileLoader.loadScenario(new BufferedReader(strReader));
        checkLoadedScenario(scenario, false);
    }

    void checkLoadedScenario(Scenario scenario, boolean bRespectWhites)
    {
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