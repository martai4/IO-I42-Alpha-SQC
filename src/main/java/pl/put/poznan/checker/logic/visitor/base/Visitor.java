package pl.put.poznan.checker.logic.visitor.base;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.SubScenario;
import pl.put.poznan.checker.scenario.Step;

/**
 * <b>Interfejs <i>Wizytatora</i></b>, po którym dziedziczą wszystkie <b>funkcjonalne <i>Wizytatory</i></b>.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public interface Visitor
{
    /**
     * Odwiedza {@link SubScenario PodScenariusz główny} {@link Scenario Scenariusza}.
     *
     * @param scenario {@link Scenario Scenariusz}, który ma odwiedzić <i>Wizytator</i>
     * @return <i>Wizytator</i> (siebie).
     */
    Visitor visit(Scenario scenario);

    /**
     * Odwiedza wszystkie {@link Step Kroki} {@link SubScenario PodScenariusza}.
     *
     * @param subScenario {@link SubScenario PodScenariusz}, który ma odwiedzić <i>Wizytator</i>
     * @return <i>Wizytator</i> (siebie).
     */
    Visitor visit(SubScenario subScenario);

    /**
     * Odwiedza {@link Step Krok}.
     *
     * @param step {@link Step Krok}, który ma odwiedzić <i>Wizytator</i>
     * @return <i>Wizytator</i> (siebie).
     */
    Visitor visit(Step step);
}