package pl.put.poznan.checker.logic.visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.ScenarioFormat;
import pl.put.poznan.checker.logic.visitor.base.Visitor;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

/**
 * {@link Visitor Wizytator} odpowiedzialny za sprawdzanie <b>słów kluczowych</b>
 * {@link Scenario Scenariusza}.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class DecisionsVisitor implements Visitor
{
    private static final Logger logger = LoggerFactory.getLogger(DecisionsVisitor.class);
    private int decisionsCount = 0;

    /**
     * Domyślny konstruktor <code>LengthVisitor</code>.
     */
    public DecisionsVisitor() {}

    /**
     * Sprawdza, czy podany <b>tekst</b> zaczyna się od <b>słowa kluczowego</b> np.:
     * <p><i><b>FOR</b> Aktor robi coś</i> - <code>true</code><br>
     * <i>Aktor robi coś</i> - <code>false</code></p>
     * @param stepText badany tekst
     * @return <code>true</code>, gdy podany <b>tekst</b> zaczyna się od <b>aktora</b> lub
     * <b>słowa kluczowego</b> z <b>aktorem</b>, <code>false</code> w przeciwnym wypadku
     */
    private boolean isDecision(String stepText)
    {
        //Pozbywamy się *białych znaków* na początku *tekstu*, jeżeli ich ignorowanie zostało ustawione
        if (ScenarioFormat.bIgnoreExcessWhitechars)
            while (Character.isWhitespace(stepText.charAt(0))) {
                stepText = stepText.substring(1);
                logger.info("Usunięto biały znak z początku kroku.");
            }

        //Sprawdza, czy *słowo kluczowe* znajduje się na początku *zmodyfikowanego tekstu*
        for (String key : ScenarioFormat.keys)
            if (stepText.indexOf(key) == 0){
                logger.info("Krok rozpoczyna się od słowa kluczowego {}",key);
                return true;
            }
        logger.info("Krok nie rozpoczyna się od słowa kluczowego {}",stepText);
        return false;
    }

    /**
     * Odwiedza {@link SubScenario PodScenariusz} główny {@link Scenario Scenariusza}, którego <b>aktorów</b> sprawdzamy.
     *
     * @param scenario {@link Scenario Scenariusz}, który ma odwiedzić <i>Wizytator</i>
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
     * @param subScenario {@link SubScenario PodScenariusz}, który ma odwiedzić <i>Wizytator</i>
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

        for (Step s : subScenario.getSteps())
            visit(s);

        return this;
    }

    /**
     * Odwiedza {@link Step Krok} i sprawdza, czy zaczyna się od <b>słowa kluczowego</b>.
     *
     * @param step {@link Step Krok}, który ma odwiedzić <i>Wizytator</i>
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(Step step)
    {
        //Zwiększa licznik decyzji, jeżeli Krok zaczyna się od słowa kluczowego
        if (isDecision(step.getText())) {
            ++decisionsCount;
        }

        //Odwiedza PodScenariusz
        SubScenario subScenario = step.getChild();
        if (subScenario != null) {
            logger.info("Przechodzę do podscenariusza.");
            return visit(subScenario);
        }
        logger.info("Odwiedzono wszystkie podscenariusze dla kroku.");

        return this;
    }

    public int getDecisionsCount() { return decisionsCount; }
}
