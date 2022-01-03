package pl.put.poznan.checker.logic.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.visitor.base.Visitor;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

/**
 * Implementuje wzorzec projektowy {@link Visitor Wizytator} w celu spełnienia funkcjonalności reprezentowania
 * <i>Scenariuszy</i> tylko do określonego poziomu zagłębienia.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class SubLevelsVisitor implements Visitor
{
    /**
     * Maksymalny poziom zagłębienia <i>PodScenariuszy</i>.
     */
    private final int maxLevel;

    /**
     * Aktualny poziom zagłębienia w czasie przetwarzania.
     */
    private int currentLevel;

    /**
     * Nazwa dla nowego {@link Scenario Scenariusza}.
     */
    private final String newName;

    /**
     * {@link SubScenario PodScenariusz} będący rodzicem dla aktualnie przetwarzanego elementu.
     */
    private SubScenario currentParent;

    /**
     * Wynikowy {@link Scenario Scenariusz}, o poziomie zagłębienia <code>maxLevel</code>.
     */
    private Scenario result;

    /**
     * <i>Logger</i> dokumentujący pracę klasy.
     */
    private final Logger logger;

    /**
     * Konstruktor określający podstawowe parametry.
     *
     * @param maxLevel maksymalny poziom zagłębienia, jeśli &lt; 1: rzuca wyjątek
     * @param name     nazwa dla nowej wersji {@link Scenario Scenariusza}
     * @throws Exception wyjątek rzucany w przypadku podania nieprawidłowego poziomu maksymalnego zagłębienia
     */
    public SubLevelsVisitor(int maxLevel, String name) throws Exception
    {
        logger = LoggerFactory.getLogger(SubLevelsVisitor.class);
        this.newName = name;
        this.currentLevel = 0;
        this.result = null;

        if (maxLevel < 1)
        {
            logger.debug("Nieprawidłowy parametr maxLevel: {}, nie może być mniejszy niż 1", maxLevel);
            throw new Exception();
        }
        else this.maxLevel = maxLevel;

        logger.info("Utworzono obiekt klasy {}", this.getClass());
    }

    /**
     * Implementacja interfejsu wzorca projektowego {@link Visitor Wizytator}. Podany {@link Scenario Scenariusz} zostaje
     * zwrócony z <b>nową nazwą</b> i <b>poziomem zagłębienia</b> {@link SubScenario PodScenariuszy} do poziomu <code>maxLevels</code>.
     *
     * @param toConvert <i>Scenariusz</i>, który ma zostać przetworzony
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(Scenario toConvert)
    {
        if (toConvert == null)
        {
            logger.warn("Próbowano odwiedzić Scenariusz, który nie istnieje");
            return this;
        }

        logger.debug("Rozpoczęto przetwarzanie Scenariusza {}", toConvert.getName());
        SubScenario mainScenario = new SubScenario();
        if (toConvert.getMain() != null)
            result = new Scenario(newName, toConvert.getActors(), toConvert.getSystemActor(), mainScenario);
        currentParent = mainScenario;

        Visitor visitor = visit(toConvert.getMain());
        logger.debug("Zakończono przetwarzanie Scenariusza {}", toConvert.getName());

        return visitor;
    }

    /**
     * Implementacja interfejsu wzorca projektowego {@link Visitor Wizytator}.
     *
     * @param subScenario Aktualnie przetwarzany {@link SubScenario PodScenariusz}
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(SubScenario subScenario)
    {
        if (currentLevel == maxLevel)
            return this;

        if (subScenario == null)
        {
            logger.warn("Próbowano odwiedzić PodScenariusz, który nie istnieje");
            return this;
        }
        ++currentLevel;
        SubScenario parentForThis = currentParent;
        for (Step step : subScenario.getSteps())
        {
            visit(step);
            currentParent = parentForThis;  //wraca z powrotem do przetwarzania oryginalnego PodScenariusza
        }

        --currentLevel;
        return this;
    }

    /**
     * Implementacja interfejsu wzorca projektowego {@link Visitor Wizytator}.
     *
     * @param step aktualnie przetwarzany {@link Step Krok}
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(Step step)
    {
        if (step.getChild() == null)
            currentParent.addStep(step);

        else if (currentLevel != maxLevel)
        {
            SubScenario newStepSubscenario = new SubScenario();
            SubScenario parentForThis = currentParent;
            currentParent = newStepSubscenario;

            step.getChild().acceptVisitor(this);

            currentParent = parentForThis;
            currentParent.getSteps().add(new Step(step.getText(), newStepSubscenario));
        }

        return this;
    }

    /**
     * Funkcja zwraca wynikowy {@link Scenario Scenariusz}.
     *
     * @return Przetworzony <i>Scenariusz</i>.
     */
    public Scenario getConverted()
    {
        logger.debug("Zwrócenie Scenariusza \"{}\"", newName);
        return result;
    }

}