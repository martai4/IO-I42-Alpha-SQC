package pl.put.poznan.checker.scenario;

import pl.put.poznan.checker.logic.visitor.base.VisitableElement;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

/**
 * Pojedynczy krok scenariusza
 *
 * @author I42-Alpha
 * @version 1.1
 */
public class Step implements VisitableElement {
    private String text;
    /**
     * {@link pl.put.poznan.checker.scenario.SubScenario Podscenariusz} powiazany z krokiem.
     */
    private SubScenario child;

    /**
     * Domyslny konstruktor Step.
     */
    public Step() { }

    /**
     * Konstruktor Step, ustawia zawartosc tekstowa.
     * @param stepText zawartosc kroku
     */
    public Step(String stepText) {
        this.text = stepText;
    }

    /**
     * Konstruktor Step, ustawia zawartosc tekstowa i podscenariusz.
     * @param stepText zawartosc kroku
     * @param stepChild podscenariusz powiazany z krokiem
     */
    public Step(String stepText, SubScenario stepChild) {
        this.text = stepText; this.child = stepChild;
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
