package pl.put.poznan.checker.scenario;

import pl.put.poznan.checker.logic.visitor.base.VisitableElement;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

/**
 * Pojedynczy krok scenariusza
 *
 * @author I42-Alpha
 * @version 1.0
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

    public boolean Is_w2_start_w1(String w1, String w2){
        for (int i=0;i<w1.length();i++){
            if(w1.charAt(i) != w2.charAt(i)){
                return false;
            }
        }
        return true;
    }

    public boolean IS_start_from_decision(){
        String k1="IF";
        String k2="ELSE";
        String k3="FOR EACH";
        if(Is_w2_start_w1(k1, text) || Is_w2_start_w1(k2,text) || Is_w2_start_w1(k3,text)){
            return true;
        }
        return false;
    }

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
