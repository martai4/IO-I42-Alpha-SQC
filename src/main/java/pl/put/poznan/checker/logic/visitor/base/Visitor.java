package pl.put.poznan.checker.logic.visitor.base;

import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

//todo: przerzucić do logiki
public interface Visitor {
    Visitor visit(Scenario scenario);

    Visitor visit(SubScenario subScenario);

    Visitor visit(Step step);
}