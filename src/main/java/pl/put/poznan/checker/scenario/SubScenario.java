package pl.put.poznan.checker.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Kawałek scenariusza, który zostaje wywoływany jako część wybranego kroku
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class SubScenario {
    private List<Step> steps = new ArrayList<>();

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }

    public Step getStep(Integer step) {
        return steps.get(step);
    }

    //todo: sprawdzić błędy (próba dostania się do elementu poza zakresem)
    public void setStep(Integer index, Step step) {
        this.steps.set(index, step);
    }

    public void addStep(Step step) {
        this.steps.add(step);
    }

    public void addStep(String stepText) {
        this.steps.add(new Step(stepText));
    }

    public Integer getLength() {
        return steps.size();
    }
}
