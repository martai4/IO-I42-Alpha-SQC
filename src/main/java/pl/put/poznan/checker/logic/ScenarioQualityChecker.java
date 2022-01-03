package pl.put.poznan.checker.logic;
import pl.put.poznan.checker.logic.visitor.*;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;

import java.util.List;

/**
 * Odpowiada za logikę sprawdzania <b>poprawności</b> {@link Scenario Scenariuszy}.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class    ScenarioQualityChecker
{
    /**
     * Domyślny konstruktor <code>ScenarioQualityChecker</code>.
     */
    public ScenarioQualityChecker() {}

    /**
     * Zwraca <b>listę błędów</b> powiązanych z <b>aktorami</b> w całym {@link Scenario Scenariuszu}, przy pomocy
     * {@link ActorVisitor ActorVisitor}.
     * @param scenario <i>Scenariusz</i> do sprawdzenia błędów
     * @return <b>Lista błędów</b> powiązanych z <b>aktorami</b> w całym <i>Scenariuszu</i>.
     */
    public static List<String> getActorErrors(Scenario scenario)
    {
        ActorVisitor visitor = new ActorVisitor(scenario.getActors(), scenario.getSystemActor());
        scenario.acceptVisitor(visitor);
        return visitor.getErrors();
    }

    /**
     * Zwraca <b>liczbę decyzji</b> - {@link Step Kroków} {@link Scenario Scenariusza} zaczynających się od <b>słowa kluczowego</b>,
     * przy pomocy {@link DecisionsVisitor DecisionsVisitor}.
     * @param scenario <i>Scenariusz</i> do sprawdzenia <b>liczby decyzji</b>
     * @return <b>Liczba decyzji</b> w <i>Scenariuszu</i>.
     */
    public static int getDecisionCount(Scenario scenario)
    {
        DecisionsVisitor visitor = new DecisionsVisitor();
        scenario.acceptVisitor(visitor);
        return visitor.getDecisionsCount();
    }

    /**
     * Zwraca <b>rozmiar</b> {@link Scenario Scenariusza}, przy pomocy {@link LengthVisitor LengthVisitor}.
     * @param scenario <i>Scenariusz</i> do sprawdzenia <b>rozmiaru</b>
     * @return Liczba {@link Step Kroków} w <i>Scenariuszu</i>.
     */
    public static int getScenarioSize(Scenario scenario)
    {
        LengthVisitor visitor = new LengthVisitor();
        scenario.acceptVisitor(visitor);
        return visitor.getSize();
    }

    /**
     * Zwraca {@link Scenario Scenariusz} w formie <b>tekstu z numeracją</b> {@link Step <b>Kroków</b>}, przy pomocy
     * {@link ScenarioTextifier ScenarioTextifier}.
     * @param scenario <i>Scenariusz</i> do zamiany w <b>tekst</b>
     * @return Ciąg znaków, zawierający cały <i>Scenariusz</i>.
     */
    public static String getScenarioTextified(Scenario scenario)
    {
        ScenarioTextifier visitor = new ScenarioTextifier();
        scenario.acceptVisitor(visitor);
        return visitor.getText();
    }

    /**
     * Zwraca {@link Scenario Scenariusz} w formie <b>tekstu z numeracją</b> {@link Step <b>Kroków</b>}, przy pomocy
     * {@link ScenarioTextifier ScenarioTextifier}.
     * @param scenario <i>Scenariusz</i> do zamiany w <b>tekst</b>
     * @return Nowy <i>Scenariusz</i> z <b>nową nazwą</b> i ograniczony do określonego <b>poziomu zagłębienia</b>.
     */
    public static Scenario getScenarioUpToLevel(Scenario scenario, int level) throws Exception
    {
        SubLevelsVisitor visitor = new SubLevelsVisitor(level, scenario.getName());
        scenario.acceptVisitor(visitor);
        return visitor.getConverted();
    }
}
