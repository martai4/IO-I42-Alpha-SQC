package pl.put.poznan.checker.logic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.SubScenario;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Odpowiada za logikę wczytywania, sprawdzania poprawności i przetwarzania scenariuszy
 *
 * @author I42-Alpha
 * @version 1.2
 */
public class ScenarioQualityChecker
{
    public int getScenarioSize(Scenario scenario)
    {
        LengthVisitor visitor = new LengthVisitor();
        scenario.acceptVisitor(visitor);
        return visitor.getSize();
    }
}
