package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Zbior wszystkich {@link pl.put.poznan.checker.scenario.Scenario scenariuszy} wczytanych przez program.
 *
 * @author I42-Alpha
 * @version 1.0
 */
@Component
public class ScenarioRepository
{
    private static final Logger logger = LoggerFactory.getLogger(ScenarioRepository.class);
    private final List<Scenario> scenarios = new ArrayList<>();

    /**
     * Domyslny konstruktor ScenarioRepository.
     */
    public ScenarioRepository(){

    }

    /**
     * Dodaje {@link pl.put.poznan.checker.scenario.Scenario scenariusz} do repozytorium.
     * @param scenario dodany scenariusz
     */
    public void addScenario(Scenario scenario)
    {
        if (scenario == null)
        {
            logger.warn("addScenario(Scenario) probowano dodac pusty scenariusz");
            return;
        }
        logger.warn("addScenario(Scenario) probuje dodac nowy scenariusz o nazwie \"" + scenario.getName() + "\"");
        //todo: check poprawności scenariusza (za pomoca ScenarioQualityChecker)
        //Integer id = lastGeneratedId.incrementAndGet();

        //scenarios.put(id, scenario);
        scenarios.add(scenario);
    }

    /**
     * Zwraca rozmiar repozytorium.
     * @return Rozmiar repozytorium.
     */
    public Integer getLength()
    {
        return scenarios.size();
    }

    /**
     * Zwraca {@link pl.put.poznan.checker.scenario.Scenario scenariuszy} po podaniu jego id.
     * @param id identyfikator scenariusza
     * @return Scenariusz z repozytorium.
     */
    public Scenario getScenario(Integer id) {
        if (id >= getLength())
        {
            logger.warn("getScenario(Integer) probowal zwrocic nieistniejacy scenariusz o numerze " + id.toString());
            return null;
        }
        return scenarios.get(id);
    }

    /**
     * Znajduje scenariusz przy pomocy jego tytulu
     * @param name tytul scenariusza
     * @return scenariusz
     */
    public Scenario getScenarioByName(String name) {
        if (scenarios.stream().anyMatch(scenario -> scenario.getName().equals(name)))
            return scenarios.stream().filter(scenario -> scenario.getName().equals(name)).findFirst().get();
        logger.warn("getScenarioByName(String) probowal zwrocic nieistniejacy scenariusz o nazwie \""
                + name.toString() + "\"");
        return null;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }
}
