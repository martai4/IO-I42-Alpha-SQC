package pl.put.poznan.checker.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Pełen scenariusz
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class Scenario {
    private String name;
    private List<String> actors = new ArrayList<>();
    private String systemActor;
    private SubScenario main;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //todo: uzgodnić, czy te gettery i settery są potrzebne, gdy mamy dostęp do listy za pomocą innych metod, których
    //todo: zaletą jest (będzie) obsługa błędów
    /*public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }*/

    //todo: sprawdzić błędy (próba dostania się do elementu poza zakresem)
    /**
     * @param index numer aktora, którego chcemy zwrócić. Aktorzy są numerowani w kolejności wprowadzenia
     * @return zwraca nazwę aktora z listy wszystkich aktorów
     */
    public String getActor(Integer index) {
        return actors.get(index);
    }

    //todo: sprawdzić błędy (próba dostania się do elementu poza zakresem)
    /**
     * @param index numer aktora, którego chcemy zwrócić. Aktorzy są numerowani w kolejności wprowadzenia
     * @param actor nazwa aktora, który zostanie dodany pod wskazany index
     */
    public void setActor(Integer index, String actor) { actors.set(index, actor);
    }

    /**
     * @param actor nazwa aktora, który zostanie dodany na koniec listy
     */
    public void addActor(String actor) { actors.add(actor);
    }

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
