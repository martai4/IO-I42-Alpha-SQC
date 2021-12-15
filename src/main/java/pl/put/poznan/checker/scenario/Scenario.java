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

    private String name; /** Tytuł scenariusza */
    private final List<String> actors = new ArrayList<>();
    private String systemActor;
    /** Zawartość scenariusza znajduje się w obiekcie klasy SubScenario, nawet jeżeli ten nie posiada podscenariuszy */
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
     * Zwraca aktora z listy aktorów scenariusza
     * @param index numer aktora, którego chcemy zwrócić. Aktorzy są numerowani w kolejności wprowadzenia
     * @return nazwa aktora
     */
    public String getActor(Integer index) {
        return actors.get(index);
    }

    //todo: sprawdzić błędy (próba dostania się do elementu poza zakresem)
    /**
     * Nadpisuje wybranego aktora w liście aktorów
     * @param index indeks aktora, który zostanie ndapisany
     * @param actor nowa nazwa aktora
     */
    public void setActor(Integer index, String actor) { actors.set(index, actor);
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
