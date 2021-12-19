package pl.put.poznan.checker.scenario;

//todo: przerzucić do logiki
public interface Visitor {
    Visitor visit(Scenario scenario);

    Visitor visit(SubScenario subScenario);

    Visitor visit(Step step);
}