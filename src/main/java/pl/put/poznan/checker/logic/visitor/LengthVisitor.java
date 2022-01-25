package pl.put.poznan.checker.logic.visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.visitor.base.Visitor;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.SubScenario;
import pl.put.poznan.checker.scenario.Step;

/**
 * {@link Visitor Wizytator} odpowiedzialny za zwracanie <b>rozmiaru</b> {@link Scenario Scenariusza}.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class LengthVisitor implements Visitor
{
    /**
     * <i>Logger</i> dokumentujący pracę klasy.
     */
    private static final Logger logger = LoggerFactory.getLogger(LengthVisitor.class);
    /**
     * @see LengthVisitor#getSize()
     */
    private int size = 0;

    /**
     * Domyślny konstruktor <code>LengthVisitor</code>.
     */
    public LengthVisitor() {}

    /**
     * Odwiedza {@link SubScenario <b>PodScenariusz</b>} <b>główny</b>
     * {@link Scenario Scenariusza}.
     *
     * @param scenario {@link Scenario Scenariusz}, którego <b>rozmiar</b> chcemy obliczyć.
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(Scenario scenario)
    {
        if (scenario == null)
        {
            logger.warn("Próbowano odwiedzić Scenariusz, który nie istnieje");
            return this;
        }
        logger.debug("Rozpoczęto przetwarzanie Scenariusza {}", scenario.getName());
        Visitor visit = visit(scenario.getMain());
        logger.debug("Zakończono przetwarzanie Scenariusza {}", scenario.getName());
        return visit;
    }

    /**
     * Odwiedza wszystkie {@link Step Kroki} {@link SubScenario PodScenariusza}.
     *
     * @param subScenario {@link SubScenario PodScenariusz}, który ma być odwiedzony
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(SubScenario subScenario)
    {
        if (subScenario == null)
        {
            logger.warn("Próbowano odwiedzić PodScenariusz, który nie istnieje");
            return this;
        }

        for (Step step : subScenario.getSteps())
            step.acceptVisitor(this);

        return this;
    }

    /**
     * Zwiększa licznik {@link Step Kroków} o jeden i odwiedzia {@link SubScenario PodScenariusz},
     * jeżeli <i>Krok</i> go posiada.
     *
     * @param step {@link Step Krok}, który ma odwiedzić
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(Step step)
    {
        ++size;
        SubScenario subScenario = step.getChild();
        if (subScenario != null)
            return subScenario.acceptVisitor(this);

        return this;
    }

    /**
     * Zwraca <b>rozmiar</b>.
     *
     * @return <b>Rozmiar</b> - liczba wszystkich {@link Step Kroków} w {@link Scenario Scenariuszu}.
     */
    public int getSize()
    {
        return size;
    }
}
