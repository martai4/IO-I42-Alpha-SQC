package pl.put.poznan.checker.scenario;

import java.util.LinkedList;
import java.util.List;

public class SubScenario implements VisitableElement {
    private Integer level;
    private Integer length;
    private List<Step> steps;

    public SubScenario(){
        steps = new LinkedList<Step>();
    };

    public SubScenario(int lvl){
        this.level = lvl;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }

    // zwraca wizytatora po wykonaniu jego zadania
    @Override
    public Visitor acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
