package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.notNull;

class ScenarioQualityCheckerTest
{
    static Scenario mockScenario = mock(Scenario.class);

    static List<Step> createMockStepList(String stepsRaw)
    {
        List<Step> steps    =   new ArrayList<>();
        for (String step : stepsRaw.split("\n"))
        {
            Step mockStep = mock(Step.class);
            when(mockStep.getChild()).thenReturn(null);
            when(mockStep.getText()).thenReturn(step);
            when(mockStep.acceptVisitor(notNull())).thenCallRealMethod();
            steps.add(mockStep);
        }
        return steps;
    }

    @BeforeAll
    static void createMockScenario()
    {
        when(mockScenario.getName()).thenReturn("Dodane książki");
        when(mockScenario.getActors()).thenReturn(Arrays.asList("Bibliotekarz", "Inny", "KtośTam", "TamKtoś"));
        when(mockScenario.getSystemActor()).thenReturn("System");
        when(mockScenario.acceptVisitor(notNull())).thenCallRealMethod();
        //Przechodzimy PodScenariusze od końca. Najpierw tworzymy najbardziej zagłębiony, żeby móc go dodać do Kroku
        //PodScenariusza wyżej i budujemy w ten sposób cały Scenariusz.

        //Poziom 3.
        List<Step> steps = createMockStepList("Bibliotekarz wybiera opcję dodania egzemplarza\n" +
                "System prosi o podanie danych egzemplarza\n" +
                "Bibliotekarz podaje dane egzemplarza i zatwierdza.\n" +
                "System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.");

        SubScenario mockSubScenario = mock(SubScenario.class);
        when(mockSubScenario.acceptVisitor(notNull())).thenCallRealMethod();
        when(mockSubScenario.getSteps()).thenReturn(steps);

        //Poziom 2.
        steps = createMockStepList("Bibliotekarz wybiera opcję definiowania egzemplarzy\n" +
                "System prezentuje zdefiniowane egzemplarze\n" +
                "FOR EACH egzemplarz:");

        SubScenario oldMock = mockSubScenario;
        mockSubScenario = mock(SubScenario.class);
        when(mockSubScenario.acceptVisitor(notNull())).thenCallRealMethod();
        when(mockSubScenario.getSteps()).thenReturn(steps);
        when(steps.get(steps.size()-1).getChild()).thenReturn(oldMock);

        //Poziom 1.
        steps = createMockStepList("Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +
                "Wyświetla się formularz.\n" +
                "Bibliotekarz podaje dane książki.\n" +
                "IF Bibliotekarz pragnie dodać egzemplarze książki");

        oldMock = mockSubScenario;
        mockSubScenario = mock(SubScenario.class);
        when(mockSubScenario.acceptVisitor(notNull())).thenCallRealMethod();
        when(mockSubScenario.getSteps()).thenReturn(steps);

        when(steps.get(steps.size()-1).getChild()).thenReturn(oldMock);

        //Poziom 1. cd.
        List<Step> addSteps = createMockStepList("Bibliotekarz zatwierdza dodanie książki.\n" +
                "System informuje o poprawnym dodaniu książki.");

        steps.addAll(addSteps);
        when(mockSubScenario.acceptVisitor(notNull())).thenCallRealMethod();
        when(mockSubScenario.getSteps()).thenReturn(steps);
        when(mockScenario.getMain()).thenReturn(mockSubScenario);
    }

    @Test
    void scenarioEmpty_showActorsErrors()
    {
        assertTrue(ScenarioQualityChecker.getActorErrors(new Scenario()).isEmpty());
    }

    @Test
    void scenarioEmpty_getDecisionsCount()
    {
        assertEquals(0, ScenarioQualityChecker.getDecisionCount(new Scenario()));
    }

    @Test
    void scenarioEmpty_getScenarioSize()
    {
        assertEquals(0, ScenarioQualityChecker.getScenarioSize(new Scenario()));
    }

    @Test
    void scenarioEmpty_getScenarioTextified()
    {
        assertTrue(ScenarioQualityChecker.getScenarioTextified(new Scenario()).isBlank());
    }

    @Test
    void scenarioEmpty_getScenarioUpToLevel0()
    {
        assertThrows(Exception.class, () -> ScenarioQualityChecker.getScenarioUpToLevel(new Scenario(), 0));
    }

    @Test
    void scenarioEmpty_getScenarioUpToLevel1() throws Exception
    {
        assertNull(ScenarioQualityChecker.getScenarioUpToLevel(new Scenario(), 1));
    }

    @Test
    void scenarioSample_getActorErrors()
    {
        assertEquals(Arrays.asList("Wyświetla się formularz.", "FOR EACH egzemplarz:"),
                ScenarioQualityChecker.getActorErrors(mockScenario));
    }

    @Test
    void scenarioSample_getDecisionsCount()
    {
        assertEquals(2, ScenarioQualityChecker.getDecisionCount(mockScenario));
    }

    @Test
    void checkSample_getScenarioSize()
    {
        assertEquals(13, ScenarioQualityChecker.getScenarioSize(mockScenario));
    }

    @Test
    void scenarioSample_getScenarioTextified()
    {
        assertEquals("Tytuł: Dodane książki\n" +
                "Aktorzy: Bibliotekarz, Inny, KtośTam, TamKtoś\n" +
                "Aktor systemowy: System\n" +
                "\n" +
                "1.Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +
                "2.Wyświetla się formularz.\n" +
                "3.Bibliotekarz podaje dane książki.\n" +
                "4.IF Bibliotekarz pragnie dodać egzemplarze książki\n" +
                "4.1.Bibliotekarz wybiera opcję definiowania egzemplarzy\n" +
                "4.2.System prezentuje zdefiniowane egzemplarze\n" +
                "4.3.FOR EACH egzemplarz:\n" +
                "4.3.1.Bibliotekarz wybiera opcję dodania egzemplarza\n" +
                "4.3.2.System prosi o podanie danych egzemplarza\n" +
                "4.3.3.Bibliotekarz podaje dane egzemplarza i zatwierdza.\n" +
                "4.3.4.System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\n" +
                "5.Bibliotekarz zatwierdza dodanie książki.\n" +
                "6.System informuje o poprawnym dodaniu książki.\n", ScenarioQualityChecker.getScenarioTextified(mockScenario));
    }

    @Test
    void scenarioSample_getScenarioUpToLevel0()
    {
        assertThrows(Exception.class, () -> ScenarioQualityChecker.getScenarioUpToLevel(mockScenario, 0));
    }

    @Test
    void scenarioSample_getScenarioUpToLevel1() throws Exception
    {
        assertEquals("Tytuł: Dodane książki\n" +
                        "Aktorzy: Bibliotekarz, Inny, KtośTam, TamKtoś\n" +
                        "Aktor systemowy: System\n" +
                        "\n" +
                        "1.Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +
                        "2.Wyświetla się formularz.\n" +
                        "3.Bibliotekarz podaje dane książki.\n" +
                        "4.Bibliotekarz zatwierdza dodanie książki.\n" +
                        "5.System informuje o poprawnym dodaniu książki.\n",
                ScenarioQualityChecker.getScenarioTextified(
                        ScenarioQualityChecker.getScenarioUpToLevel(mockScenario, 1)));
    }

    @Test
    void scenarioSample_getScenarioUpToLevel2() throws Exception
    {
        assertEquals("Tytuł: Dodane książki\n" +
                        "Aktorzy: Bibliotekarz, Inny, KtośTam, TamKtoś\n" +
                        "Aktor systemowy: System\n" +
                        "\n" +
                        "1.Bibliotekarz wybiera opcje dodania nowej pozycji książkowej\n" +
                        "2.Wyświetla się formularz.\n" +
                        "3.Bibliotekarz podaje dane książki.\n" +
                        "4.IF Bibliotekarz pragnie dodać egzemplarze książki\n" +
                        "4.1.Bibliotekarz wybiera opcję definiowania egzemplarzy\n" +
                        "4.2.System prezentuje zdefiniowane egzemplarze\n" +
                        "5.Bibliotekarz zatwierdza dodanie książki.\n" +
                        "6.System informuje o poprawnym dodaniu książki.\n",
                ScenarioQualityChecker.getScenarioTextified(
                        ScenarioQualityChecker.getScenarioUpToLevel(mockScenario, 2)));
    }

    @Test
    void scenarioSample_getScenarioUpToLevel3() throws Exception
    {
        assertEquals(ScenarioQualityChecker.getScenarioTextified(mockScenario),
                ScenarioQualityChecker.getScenarioTextified(
                        ScenarioQualityChecker.getScenarioUpToLevel(mockScenario, 3)));
    }
}