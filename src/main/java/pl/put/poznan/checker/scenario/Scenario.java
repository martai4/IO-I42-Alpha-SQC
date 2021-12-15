package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Pełen scenariusz
 *
 * @author I42-Alpha
 * @version 1.1
 */
public class Scenario {
    private static final Logger logger = LoggerFactory.getLogger(Scenario.class);
    private String name; /** Tytuł scenariusza */
    private List<String> actors = new ArrayList<>();
    private String systemActor;
    /** Zawartość scenariusza znajduje się w obiekcie klasy SubScenario, nawet jeżeli ten nie posiada podscenariuszy */
    private SubScenario main;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    /**
     * Zwraca aktora z listy aktorów scenariusza
     * @param index numer aktora, którego chcemy zwrócić. Aktorzy są numerowani w kolejności wprowadzenia
     * @return nazwa aktora
     */
    public String getActor(Integer index) {
        if (index >= getActorsCount())
        {
            logger.warn("getActor(Integer index) próbował zwrócić nieistniejący element o indeksie "
                    + index.toString());
            return null;
        }
        return actors.get(index);
    }

    /**
     * Nadpisuje wybranego aktora w liście aktorów
     * @param index indeks aktora, który zostanie ndapisany
     * @param actor nowa nazwa aktora
     */
    public void setActor(Integer index, String actor) {
        if (index >= getActorsCount())
        {
            logger.warn("setActor(Integer index, String actor) próbował nadpisać nieistniejący element o indeksie "
                    + index.toString());
            return;
        }
        actors.set(index, actor);
    }

    /**
     * Dodaje aktora na koniec listy aktorów
     * @param actor nazwa aktora
     */
    public void addActor(String actor) { actors.add(actor);
    }

    /**
     * Zwraca rozmiar listy aktorów
     * @return rozmiar
     */
    public Integer getActorsCount() {
        return actors.size();
    }

    public String getSystemActor() {
        return systemActor;
    }

    public void setSystemActor(String systemActor) {
        this.systemActor = systemActor;
    }

    public SubScenario getMain() {
        return main;
    }

    public void setMain(SubScenario main) {
        this.main = main;
    }
}
