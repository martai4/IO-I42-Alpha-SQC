package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;
import pl.put.poznan.checker.scenario.Visitor;

public class LengthVisitor implements Visitor {
    private int size;
    @Override
    public Visitor visit(Scenario scenario) {
        return scenario.getMain().acceptVisitor(this);
    }

    @Override
    public Visitor visit(SubScenario subScenario) {
        for (Step step : subScenario.getSteps())
            step.acceptVisitor(this);
        return this;
    }

    @Override
    public Visitor visit(Step step) {
        SubScenario subScenario = step.getChild();
        if (subScenario != null)
            subScenario.acceptVisitor(this);
        ++size;
        return this;
    }

    int getSize(){
        return size;
    }
}
