package pl.put.poznan.checker.logic;
import pl.put.poznan.checker.logic.visitor.LengthVisitor;
import pl.put.poznan.checker.scenario.Scenario;

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
     * {@link LengthVisitor LengthVisitor}.
     */
    public int getScenarioSize(Scenario scenario)
    {
        LengthVisitor visitor = new LengthVisitor();
        scenario.acceptVisitor(visitor);
        return visitor.getSize();
    }
}
