package pl.put.poznan.checker.scenario;

import pl.put.poznan.checker.logic.visitor.base.VisitableElement;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

/**
 * Pojedyńczy <i>Krok</i> {@link Scenario Scenariusza}.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class Step implements VisitableElement
{
    /**
     * <b>Zawartość tekstowa</b> <i>Kroku</i>.
     */
    private String text;

    /**
     * {@link SubScenario PodScenariusz} powiązany z <i>Krokiem</i>.
     */
    private SubScenario child;

    /**
     * Domyślny konstruktor <code>Step</code>.
     */
    public Step() { }

    /**
     * Konstruktor <code>Step</code>, który ustawia <b>zawartość tekstową</b>.
     * @param stepText zawartość <i>Kroku</i>
     */
    public Step(String stepText)
    {
        this.text = stepText;
    }

    /**
     * Konstruktor <code>Step</code>, który ustawia <b>zawartość tekstową</b> i {@link SubScenario PodScenariusz}.
     * @param stepText zawartość <i>Kroku</i>
     * @param stepChild PodScenariusz powiązany z <i>Krokiem</i>
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
