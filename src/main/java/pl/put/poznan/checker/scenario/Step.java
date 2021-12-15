package pl.put.poznan.checker.scenario;

/**
 * Pojedyńczy krok scenariusza
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class Step {

    private String text;
    /**
     * Podscenariusz powiązany z krokiem
     */
    private SubScenario child;

    /**
     * Konstruktor klasy Step
     */
    Step() { }

    /**
     * Konstruktor klasy Step
     * @param stepText zawartość kroku
     */
    Step(String stepText) {
        this.text = stepText;
    }

    /**
     * Konstruktor klasy Step
     * @param stepText zawartość kroku
     * @param stepChild podscenariusz powiązany z krokiem
     */
    Step(String stepText, SubScenario stepChild) {
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
}
