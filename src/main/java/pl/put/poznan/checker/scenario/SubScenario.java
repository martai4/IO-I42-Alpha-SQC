package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Kawalek {@link pl.put.poznan.checker.scenario.Scenario scenariusza}, ktory zostaje wywolywany jako czesc przetwarzania
 * pojedynczego {@link pl.put.poznan.checker.scenario.Step kroku}. Wyjatkiem jest podscenariusz glowny scenariusza, od
 * ktorego zaczyna sie przetwarzanie.
 *
 * @author I42-Alpha
 * @version 1.1
 */
public class SubScenario implements VisitableElement
{
    private static final Logger logger = LoggerFactory.getLogger(SubScenario.class);
    private List<Step> steps = new ArrayList<>();

    /**
     * Domyslny konstruktor SubScenario.
     */
    public SubScenario(){

    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }
    /**
     * Zwraca wybrany {@link pl.put.poznan.checker.scenario.Step krok} podscenariusza.
     * @param index indeks kroku
     * @return Krok z listy wszystkich krokow podscenariusza.
     */
    public Step getStep(Integer index) {
        if (index >= getLength())
        {
            logger.warn("getStep(Integer) probowal zwrocic nieistniejacy element o indeksie " + index.toString());
            return null;
        }
        return steps.get(index);
    }
    /**
     * Nadpisuje wybrany {@link pl.put.poznan.checker.scenario.Step krok} podscenariusza.
     * @param index indeks kroku, ktory zostanie nadpisany
     * @param step nowy krok
     */
    public void setStep(Integer index, Step step) {
        if (index >= getLength())
        {
            logger.warn("setStep(Integer, Step) probowal nadpisac nieistniejacy element o indeksie "
                    + index.toString());
            return;
        }
        this.steps.set(index, step);
    }
    /**
     * Dodaje {@link pl.put.poznan.checker.scenario.Step krok} na koniec listy krokow.
     * @param step nowy krok
     */
    public void addStep(Step step) {
        this.steps.add(step);
    }
    /**
     * Dodaje {@link pl.put.poznan.checker.scenario.Step krok} na koniec listy krokow, tworzac go z podanego tekstu.
     * @param stepText tekst nowego kroku
     */
    public void addStep(String stepText) {
        this.steps.add(new Step(stepText));
    }
    /**
     * Zwraca rozmiar listy {@link pl.put.poznan.checker.scenario.Step krokow}.
     * @return Rozmiar listy.
     */
    public Integer getLength() {
        return steps.size();
    }

    @Override
    public Visitor acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
