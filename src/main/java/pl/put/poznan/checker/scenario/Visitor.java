package pl.put.poznan.checker.scenario;

public interface Visitor {
    Visitor visit(Scenario scenario);

    Visitor visit(SubScenario subScenario);

    Visitor visit(Step step);
}