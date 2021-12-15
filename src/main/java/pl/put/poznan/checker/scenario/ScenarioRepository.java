package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Zbiór wszystkich scenariuszy wczytanych przez program
 *
 * @author I42-Alpha
 * @version 1.1
 */
@Component
public class ScenarioRepository
{
    private static final Logger logger = LoggerFactory.getLogger(ScenarioRepository.class);
    //private final Map<Integer, Scenario> scenarios = new ConcurrentHashMap<>(); //todo: czy to powinien być słownik?
    private final List<Scenario> scenarios = new ArrayList<>();
    //private final AtomicInteger lastGeneratedId = new AtomicInteger(0);

    /**
     * Dodaje scenariusz do repozytorium
     * @param scenario dodany scenariusz
     */
    public void addScenario(Scenario scenario)
    {
        //todo: check poprawności scenariusza (za pomocą ScenarioQualityChecker)
        //Integer id = lastGeneratedId.incrementAndGet();

        //scenarios.put(id, scenario);
        scenarios.add(scenario);
    }

    /**
     * Zwraca rozmiar repozytorium
     * @return rozmiar
     */
    public Integer getLength()
    {
        return scenarios.size();
    }

    /**
     * Zwraca scenariusz po podaniu jego id
     * @param id identyfikator scenariusza
     * @return scenariusz
     */
    public Scenario getScenario(Integer id) {
        if (id >= getLength())
        {
            logger.warn("getScenario(Integer id) próbował zwrócić nieistniejący scenariusz o numerze " + id.toString());
            return null;
        }
        return scenarios.get(id);
    }

    /**
     * Znajduje scenariusz przy pomocy jego tytułu
     * @param name tytuł scenariusza
     * @return scenariusz
     */
    public Scenario getScenarioByName(String name) {
        if (scenarios.stream().anyMatch(scenario -> scenario.getName().equals(name)))
            return scenarios.stream().filter(scenario -> scenario.getName().equals(name)).findFirst().get();
        logger.warn("getScenarioByName(String name) próbował zwrócić nieistniejący scenariusz o nazwie \""
                + name.toString() + "\"");
        return null;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }
}
