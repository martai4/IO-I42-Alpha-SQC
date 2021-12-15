package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Kawałek scenariusza, który zostaje wywoływany jako część wybranego kroku
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class SubScenario implements VisitableElement
{
    private static final Logger logger = LoggerFactory.getLogger(SubScenario.class);
    private List<Step> steps = new ArrayList<>();

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }

    /**
     * Zwraca wybrany krok podscenariusza
     * @param index indeks kroku
     * @return krok
     */
    public Step getStep(Integer index) {
        if (index >= getLength())
        {
            logger.warn("getStep(Integer index) próbował zwrócić nieistniejący element o indeksie " + index.toString());
            return null;
        }
        return steps.get(index);
    }
    /**
     * Nadpisuje wybrany krok scenariusza
     * @param index indeks kroku, który zostanie nadpisany
     * @param step nowy krok
     */
    public void setStep(Integer index, Step step) {
        if (index >= getLength())
        {
            logger.warn("setStep(Integer index, Step step) próbował nadpisać nieistniejący element o indeksie "
                    + index.toString());
            return;
        }
        this.steps.set(index, step);
    }
    /**
     * Dodaje krok na koniec listy kroków
     * @param step nowy krok
     */
    public void addStep(Step step) {
        this.steps.add(step);
    }
    /**
     * Dodaje krok na koniec listy kroków, tworząc go z podanego tekstu
     * @param stepText tekst nowego kroku
     */
    public void addStep(String stepText) {
        this.steps.add(new Step(stepText));
    }
    /**
     * Zwraca rozmiar listy kroków
     * @return rozmiar
     */
    public Integer getLength() {
        return steps.size();
    }

    @Override
    public Visitor acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
