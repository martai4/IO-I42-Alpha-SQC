package pl.put.poznan.checker.scenario;

/**
 * Pojedyńczy krok scenariusza
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class Step {
    private String text;
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

    /**
     * @return zawartość kroku
     */
    public String getText() {
        return text;
    }

    /**
     * @param text zawartość kroku
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return podscenariusz powiązany z krokiem
     */
    public SubScenario getChild() {
        return child;
    }

    /**
     * @param child podscenariusz powiązany z krokiem
     */
    public void setChild(SubScenario child) {
        this.child = child;
    }
}
