package pl.put.poznan.checker.logic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.SubScenario;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Odpowiada za logike wczytywania, sprawdzania poprawnosci i przetwarzania {@link pl.put.poznan.checker.scenario.Scenario scenariuszy}.
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class ScenarioQualityChecker
{
    /**
     * Domyslny konstruktor ScenarioQualityChecker.
     */
    public ScenarioQualityChecker(){

    }
    /**
     * Zwraca rozmiar {@link pl.put.poznan.checker.scenario.Scenario scenariusza} przy pomocy obiektu
     * {@link pl.put.poznan.checker.logic.LengthVisitor LengthVisitor}.
     */
    public int getScenarioSize(Scenario scenario)
    {
        LengthVisitor visitor = new LengthVisitor();
        scenario.acceptVisitor(visitor);
        return visitor.getSize();
    }
}
