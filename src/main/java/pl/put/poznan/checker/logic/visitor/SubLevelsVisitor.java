package pl.put.poznan.checker.logic.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.visitor.base.Visitor;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

/**
 * Klasa implementuje wzorzec projektowy "Wizytator" w celu spelnienia funkcjonalnosci reprezentowania scenariuszy
 * tylko do okreslonego poziomu zaglebienia.
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class SubLevelsVisitor implements Visitor {

    /**
     * Maksymalny poziom zaglebienia podscenariuszy
     */
    private final int maxLevel;
    /**
     * Aktualny poziom zaglebienia w czasie przetwarzania
     */
    private int currentLevel;
    /**
     * Nazwa dla nowego scenariusza
     */
    private final String newName;
    /**
     * Rodzic dla aktualnie przetwarzanego elementu
     */
    private SubScenario currentParent;
    /**
     * Wynikowy scenariusz, o poziomie zaglebienia okreslonym przez wywolanie
     */
    private Scenario result;
    /**
     * Logger dokumentujacy prace klasy
     */
    private final Logger logger;

    /**
     * Konstruktor okreslajacy podstawowe parametry.
     *
     * @param maxLevel Maksymalny poziom zaglebienia, jesli &lt; 1 rzuca wyjatek
     * @param name     Nazwa dla nowej wersji scenariusza
     * @throws Exception Wyjatek rzucany w przypadku podania nieprawidlowego poziomu maksymalnego
     */
    public SubLevelsVisitor(int maxLevel, String name) throws Exception {
        logger = LoggerFactory.getLogger(SubLevelsVisitor.class);
        this.newName = name;
        this.currentLevel = 0;
        this.result = null;
        if (maxLevel < 1) {
            logger.debug("Nieprawidlowy parametr maxLevel: {}, nie powinien byc mniejszy niz 1", maxLevel);
            throw new Exception();
        } else {
            this.maxLevel = maxLevel;
        }
        logger.info("Utworzono obiekt klasy {}", this.getClass());
    }

    /**
     * Implementacja interfejsu wzorca projektowego wizytator. Podany scenariusz zostaje zwrocony z nowa nazwa z
     * poziomem zaglebienia podscenariuszy do poziomu okreslonego podczas wywolania konstruktora.
     *
     * @param toConvert Scenariusz, ktory ma zostac przetworzony
     * @return
     */
    @Override
    public Visitor visit(Scenario toConvert) {
        logger.info("Rozpoczeto przetwarzanie scenariusza {}", toConvert.getName());
        SubScenario mainScenario = new SubScenario();
        this.result = new Scenario(newName, toConvert.getActors(), toConvert.getSystemActor(), mainScenario);
        currentParent = mainScenario;
        toConvert.getMain().acceptVisitor(this);
        logger.info("Zakonczono przetwarzanie scenariusza {}", toConvert.getName());
        return this;
    }

    /**
     * Implementacja interfejsu wzorca projektowego wizytator
     *
     * @param subScenario Aktualnie przetwarzany podscenariusz
     * @return
     */
    @Override
    public Visitor visit(SubScenario subScenario) {
        SubScenario parentForThis = currentParent;
        for (Step step : subScenario.getSteps()) {
//            this.parents.add(subScenario);
            step.acceptVisitor(this);
            currentParent = parentForThis;
        }
        return this;
    }

    /**
     * Implementacja interfejsu wzorca projektowego wizytator
     *
     * @param step Aktualnie przetwarzany punkt scenariusza
     * @return
     */
    @Override
    public Visitor visit(Step step) {
        this.currentLevel += 1;
        if (this.currentLevel < this.maxLevel) {
            if (step.getChild() == null) {
                currentParent.getSteps().add(step);
            } else {
                SubScenario newStepSubscenario = new SubScenario();
                SubScenario parentForThis = currentParent;
                currentParent = newStepSubscenario;
                step.getChild().acceptVisitor(this);
                currentParent = parentForThis;
                currentParent.getSteps().add(new Step(step.getText(), newStepSubscenario));
            }
        } else {
            currentParent.getSteps().add(new Step(step.getText(), null));
        }
        this.currentLevel -= 1;
        return this;
    }

    /**
     * Funkcja zwraca wynikowy scenariusz
     *
     * @return Przetworzony scenariusz
     */
    public Scenario getConverted() {
        logger.info("Zwrocenie scenariusza \"{}\"", this.newName);
        return this.result;
    }
}
