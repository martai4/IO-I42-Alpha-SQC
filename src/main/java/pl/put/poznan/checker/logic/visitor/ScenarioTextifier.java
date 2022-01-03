package pl.put.poznan.checker.logic.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

/**
 * Implementuje wzorzec projektowy {@link Visitor Wizytator} w celu spełnienia funkcjonalności reprezentowania
 * <i>Scenariuszy</i> tylko do określonego poziomu zagłębienia.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class ScenarioTextifier implements Visitor
{
    private static final Logger logger = LoggerFactory.getLogger(ScenarioTextifier.class);

    /**
     * @see ScenarioTextifier#getText()
     */
    private String text;
    private String stepPrefix;

    /**
     * Zwraca utworzony {@link Scenario Scenariusz} w postaci {@link ScenarioTextifier#text tekstu}.
     * @return <i>Scenariusz</i> w postaci {@link ScenarioTextifier#text tekstu}.
     */
    public String getText() {
        return text;
    }

    /**
     * Domyślny konstruktor <code>ScenarioTextifier</code>.
     */
    public ScenarioTextifier()
    {
        this.text = new String();
        this.stepPrefix ="";
    }

    /**
     * Odwiedza wskazany {@link Scenario Scenariusz}, dołącza o nim informację do {@link ScenarioTextifier#text tekstu}
     * i odwiedza {@link SubScenario PodScenariusz główny}.
     *
     * @param scenario {@link Scenario Scenariusz}, który chcemy zamienić na {@link ScenarioTextifier#text tekst}
     * @return {@link ScenarioTextifier Siebie}.
     */

    @Override
    public Visitor visit(Scenario scenario)
    {
        if (scenario == null)
        {
            logger.warn("Próbowano odwiedzić Scenariusz, który nie istnieje");
            return this;
        }
        String name = scenario.getName();
        logger.debug("Odwiedzano scenariusz: {}", name);
        if (name != null)
        {
            logger.debug("Zapisano tytuł: {}", name);
            this.text = this.text.concat("Tytuł: " + name + "\n");
        }
        else logger.debug("Tytuł jest null");

        boolean firstActor = true;
        for (String actor: scenario.getActors())
        {
            if (!firstActor) this.text = this.text.concat(", ");
            else this.text = this.text.concat("Aktorzy: ");
            firstActor = false;

            this.text = this.text.concat(actor);
            logger.debug("Zapisano aktora: {}",actor);
        }

        if (firstActor) logger.debug("Brak aktorów");
        this.text = this.text.concat("\n");

        String systemActor = scenario.getSystemActor();
        if(systemActor!=null)
        {
            this.text = this.text.concat("Aktor systemowy: " + systemActor + "\n");
            logger.debug("Dodano aktora systemowego: {}",systemActor);
        }
        else logger.debug("Brak aktora systemowego");

        this.text = this.text.concat("\n");

        if(scenario.getMain() != null) scenario.getMain().acceptVisitor(this);

        logger.info("Zapisano Scenariusz \"{}\" w postaci tekstu :\n{}",name,this.text);
        return this;
    }

    /**
     * Odwiedza wskazany {@link SubScenario PodScenariusz} i zawarte w nim {@link Step Kroki}.
     *
     * @param subScenario odwiedzany <i>PodScenariusz</i>
     * @return {@link ScenarioTextifier Siebie}.
     */
    @Override
    public Visitor visit(SubScenario subScenario)
    {
        if (subScenario == null)
        {
            logger.warn("Próbowano odwiedzić PodScenariusz, który nie istnieje");
            return this;
        }

        logger.debug("Odwiedzono PodScenariusz");
        String stepPrefix = this.stepPrefix;
        for (int i = 1; i <= subScenario.getSteps().size(); ++i)
        {
            this.stepPrefix = stepPrefix.concat(i + ".");
            logger.debug("Prefix {}", this.stepPrefix);
            subScenario.getSteps().get(i-1).acceptVisitor(this);
        }
        this.stepPrefix = stepPrefix;
        return this;
    }

    /**
     * Odwiedza wskazany {@link Step Krok}, dodaje go do {@link ScenarioTextifier#text tekstu} i odwiedza zawarty w nim
     * {@link SubScenario PodScenariusz}, jeśli taki istnieje.
     *
     * @param step  odwiedzany {@link Step Krok}
     * @return {@link ScenarioTextifier Siebie}.
     */
    @Override
    public Visitor visit(Step step)
    {
        String name = step.getText();
        if(name != null)
        {
            this.text = this.text.concat(this.stepPrefix + step.getText() + "\n");
            logger.debug("Odwiedzono i dodano Krok: {}", name);
        }
        else logger.debug("Odwiedzony Krok jest pusty");

        if (step.getChild()!=null)
            step.getChild().acceptVisitor(this);
        return this;
    }
}
