package pl.put.poznan.checker.scenario;

public class Step implements VisitableElement{
    private String text;
    private SubScenario child;

    public Step(String text, SubScenario child) {
        this.text = text;
        this.child = child;
    }

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
    @Override
    public Visitor acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
