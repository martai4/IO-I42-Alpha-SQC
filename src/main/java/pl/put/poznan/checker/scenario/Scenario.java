package pl.put.poznan.checker.scenario;
import pl.put.poznan.checker.logic.visitor.base.VisitableElement;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Właściwy obiekt przetwarzania - <i>Scenariusz</i> wymagań funkcjonalnych.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class Scenario implements VisitableElement
{
    private static final Logger logger = LoggerFactory.getLogger(Scenario.class);
    private String name;
    private List<String> actors = new ArrayList<>();
    private String systemActor;

    /**
     * Zawartość <i>Scenariusza</i> znajduje się w {@link SubScenario PodScenariuszu} głównym.
     */
    private SubScenario main;

    /**
     * Domyślny konstruktor <code>Scenario</code>.
     */
    public Scenario() { }

    /**
     * Konstruktor wczytujący wszystkie dane.
     * @param name tytuł
     * @param actors lista aktorów
     * @param systemActor aktor systemowy
     * @param main {@link SubScenario PodScenariusz} główny
     */
    public Scenario(String name, List<String> actors, String systemActor, SubScenario main)
    {
        this.name = name;
        this.actors = actors;
        this.systemActor = systemActor;
        this.main = main;
    }

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
     * Zwraca wybranego <b>aktora</b> z listy aktorów <i>Scenariusza</i>.
     * @param index numer <b>aktora</b>, którego chcemy zwrócić. Aktorzy są numerowani w kolejności wprowadzenia
     * @return Nazwa aktora.
     */
    public String getActor(Integer index)
    {
        if (index >= getActorsCount())
        {
            logger.warn("Próbowano zwrócić nieistniejący element o indeksie {}", index.toString());
            return null;
        }
        return actors.get(index);
    }

    /**
     * Nadpisuje wybranego <b>aktora</b> w liście aktorów.
     * @param index <b>indeks aktora</b>, który zostanie nadpisany
     * @param actor nowa nazwa <b>aktora</b>
     */
    public void setActor(Integer index, String actor) {
        if (index >= getActorsCount())
        {
            logger.warn("Próbowano nadpisać nieistniejącego aktora o indeksie {}", index.toString());
            return;
        }
        actors.set(index, actor);
    }

    /**
     * Dodaje <b>aktora</b> na koniec listy aktorów.
     * @param actor nazwa <b>aktora</b>
     */
    public void addActor(String actor) { actors.add(actor); }

    /**
     * Zwraca <b>rozmiar</b> listy <b>aktorów</b>.
     * @return <b>Rozmiar</b> listy <b>aktorów</b>.
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

    @Override
    public Visitor acceptVisitor(Visitor visitor)
    {
        return visitor.visit(this);
    }
}
