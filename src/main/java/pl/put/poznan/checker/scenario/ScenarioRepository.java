package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Zbiór wszystkich {@link Scenario Scenariuszy} wczytanych przez program.
 *
 * @author I42-Alpha
 * @version 2.0
 */
@Component
public class ScenarioRepository
{
    private static final Logger logger = LoggerFactory.getLogger(ScenarioRepository.class);
    private final List<Scenario> scenarios = new ArrayList<>();

    /**
     * Domyślny konstruktor <code>ScenarioRepository</code>.
     */
    public ScenarioRepository()
    {

    }

    /**
     * Dodaje {@link Scenario Scenariusz} do repozytorium.
     * @param scenario dodany <i>Scenariusz</i>
     */
    public void addScenario(Scenario scenario)
    {
        if (scenario == null)
        {
            logger.warn("Próbowano dodać pusty Scenariusz do repozytorium");
            return;
        }
        logger.warn("Próbuje dodać nowy Scenariusz o nazwie \"{}\"", scenario.getName());
        scenarios.add(scenario);
    }

    /**
     * Zwraca <b>rozmiar</b> repozytorium.
     * @return <b>Rozmiar</b> repozytorium.
     */
    public Integer getLength()
    {
        return scenarios.size();
    }

    /**
     * Zwraca {@link Scenario Scenariusz} po podaniu jego <b>id</b>.
     * @param id identyfikator <i>Scenariusza</i>
     * @return <i>Scenariusz</i> z repozytorium.
     */
    public Scenario getScenario(Integer id)
    {
        if (id >= getLength())
        {
            logger.warn("Próbowano zwrócić nieistniejący Scenariusz o numerze " + id.toString());
            return null;
        }
        return scenarios.get(id);
    }

    /**
     * Znajduje {@link Scenario Scenariusz} przy pomocy jego <b>tytułu</b>.
     * @param name tytuł <i>Scenariusza</i>
     * @return <i>Scenariusz</i> o podanym <b>tytule</b>.
     */
    public Scenario getScenarioByName(String name)
    {
        if (scenarios.stream().anyMatch(scenario -> scenario.getName().equals(name)))
            return scenarios.stream().filter(scenario -> scenario.getName().equals(name)).findFirst().get();

        logger.warn("Próbowano zwrócić nieistniejący Scenariusz o nazwie \"{}\"", name.toString());
        return null;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }
}
