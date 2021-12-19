package pl.put.poznan.checker.logic.visitor;

import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

/**
 * {@link Visitor Wizytator} odpowiedzialny za zwracanie rozmiaru
 * {@link pl.put.poznan.checker.scenario.Scenario scenariusza}.
 *
 * @author I42-Alpha
 * @version 1.1
 */
public class LengthVisitor implements Visitor {
    private int size;

    /**
     * Domyslny konstruktor LengthVisitor.
     */
    public LengthVisitor(){

    }
    /**
     * Odwiedza {@link pl.put.poznan.checker.scenario.SubScenario podscenariusz} glowny
     * {@link pl.put.poznan.checker.scenario.Scenario scenariusza}.
     *
     * @param scenario {@link pl.put.poznan.checker.scenario.Scenario scenariusz}, ktorego rozmiar chcemy obliczyc
     * @return Zwraca wizytatora (siebie).
     */
    @Override
    public Visitor visit(Scenario scenario) {
        if (scenario.getName() != null)
             scenario.getMain().acceptVisitor(this);
        return this;
    }

    /**
     * Odwiedza wszystkie {@link pl.put.poznan.checker.scenario.Step kroki} {@link pl.put.poznan.checker.scenario.SubScenario podscenariusza}.
     *
     * @param subScenario {@link pl.put.poznan.checker.scenario.SubScenario podscenariusz}, ktory odwiedzil wizytator
     * @return Zwraca wizytatora (siebie).
     */
    @Override
    public Visitor visit(SubScenario subScenario) {
        for (Step step : subScenario.getSteps())
            step.acceptVisitor(this);
        return this;
    }
    /**
     * Zwieksza licznik {@link pl.put.poznan.checker.scenario.Step krokow} o jeden i odwiedzia
     * {@link pl.put.poznan.checker.scenario.SubScenario podscenariusz}, jezeli krok go posiada.
     *
     * @param step {@link pl.put.poznan.checker.scenario.Step krok}, ktory odwiedzil wizytator
     * @return Zwraca wizytatora (siebie).
     */
    @Override
    public Visitor visit(Step step) {
        SubScenario subScenario = step.getChild();
        if (subScenario != null)
            subScenario.acceptVisitor(this);
        ++size;
        return this;
    }
    /**
     * Zwraca rozmiar
     */
    public int getSize(){
        return size;
    }
}
