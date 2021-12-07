package pl.put.poznan.checker.scenario;

public class Step {
    private String text;
    private SubScenario child;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SubScenario getChild() {
        return child;
    }

    public void setChild(SubScenario child) {
        this.child = child;
    }
}
