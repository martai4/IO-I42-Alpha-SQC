package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.visitor.base.VisitableElement;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Kawałek {@link Scenario Scenariusza}, który zostaje wywoływany jako część przetwarzania pojedynczego {@link Step Kroku}.
 * Wyjątkiem jest <i>PodScenariusz</i> główny <i>Scenariusza</i>, od którego zaczyna się przetwarzanie.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class SubScenario implements VisitableElement
{
    private static final Logger logger = LoggerFactory.getLogger(SubScenario.class);
    private List<Step> steps = new ArrayList<>();

    /**
     * Domyślny konstruktor <code>SubScenario</code>.
     */
    public SubScenario() { }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }

    /**
     * Zwraca wybrany {@link Step Krok} <i>PodScenariusza</i>.
     *
     * @param index indeks <i>Kroku</i>
     * @return <i>Krok</i> z listy wszystkich <i>Kroków</i> <i>PodScenariusza</i>.
     */
    public Step getStep(Integer index)
    {
        if (index >= getLength())
        {
            logger.warn("Próbowano zwrócić nieistniejący element o indeksie {}", index.toString());
            return null;
        }
        return steps.get(index);
    }

    /**
     * Nadpisuje wybrany {@link Step Krok} <i>PodScenariusza</i>.
     *
     * @param index indeks <i>Kroku</i>, który zostanie nadpisany
     * @param step  nowy <i>Krok</i>
     */
    public void setStep(Integer index, Step step)
    {
        if (index >= getLength())
        {
            logger.warn("Próbowano nadpisać nieistniejący element o indeksie {}", index.toString());
            return;
        }
        this.steps.set(index, step);
    }

    /**
     * Dodaje {@link Step Krok} na koniec listy <i>Kroków</i>.
     *
     * @param step nowy <i>Krok</i>
     */
    public void addStep(Step step)
    {
        this.steps.add(step);
    }

    /**
     * Dodaje {@link Step Krok} na koniec listy <i>Kroków</i>, tworząc go z podanego tekstu.
     *
     * @param stepText <b>tekst</b> nowego <i>Kroku</i>
     */
    public void addStep(String stepText)
    {
        this.steps.add(new Step(stepText));
    }

    /**
     * Zwraca <b>rozmiar</b> listy {@link pl.put.poznan.checker.scenario.Step Kroków}.
     *
     * @return <b>Rozmiar</b> listy.
     */
    public Integer getLength() {
        return steps.size();
    }

    @Override
    public Visitor acceptVisitor(Visitor visitor)
    {
        return visitor.visit(this);
    }
}
