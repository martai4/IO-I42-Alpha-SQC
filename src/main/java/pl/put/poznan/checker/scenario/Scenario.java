package pl.put.poznan.checker.scenario;

import java.util.List;

public class Scenario implements VisitableElement {
    private String name;
    private List<String> actors;
    private String systemActor;
    private SubScenario main;

    public Scenario(){}

    public Scenario(String name, List<String> actors, String systemActor, SubScenario main) {
        this.name = name;
        this.actors = actors;
        this.systemActor = systemActor;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public SubScenario getMain() {
        return main;
    }

    public void setMain(SubScenario main) {
        this.main = main;
    }

    public String getSystemActor() { return systemActor; }

    public void setSystemActor(String systemActor) { this.systemActor = systemActor; }

    @Override
    public Visitor acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
