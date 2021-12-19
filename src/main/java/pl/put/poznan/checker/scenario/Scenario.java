package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.visitor.base.VisitableElement;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Wlasciwy obiekt przetwarzania - scenariusz wymagan funkcjonalnych.
 *
 * @author I42-Alpha
 * @version 1.1
 */
public class Scenario implements VisitableElement {
    private static final Logger logger = LoggerFactory.getLogger(Scenario.class);
    private String name; /** Tytul scenariusza. */
    private List<String> actors = new ArrayList<>();
    private String systemActor;
    /**
     * Zawartosc scenariusza znajduje sie w obiekcie klasy SubScenario, nawet jezeli ten nie posiada podscenariuszy.
     */
    private SubScenario main;

    /**
     * Domyslny konstruktor Scenario.
     */
    public Scenario() {
    }

    public int HowManyDecisions() {
        return main.HowManyDecisions();
    }

    //todo:poprawic funckje, zwraca napisy w postaci "pl.put.poznan.checker.scenario.Step@2f56ffe7"
    public List<String> ShowActorsErrors() {
        List<String> odpowiedz = new ArrayList<>();
        odpowiedz = main.ListNoActorsErrors(actors);
//        for (String s : odpowiedz) {
//            System.out.println(s);
//        }
        return odpowiedz;
    }

    public Scenario(String name, List<String> actors, String systemActor, SubScenario main) {
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
     * Zwraca aktora z listy aktorow scenariusza.
     * @param index numer aktora, ktorego chcemy zwrocic. Aktorzy sa numerowani w kolejnosci wprowadzenia.
     * @return Nazwa aktora.
     */
    public String getActor(Integer index) {
        if (index >= getActorsCount())
        {
            logger.warn("getActor(Integer) probowal zwrocic nieistniejacy element o indeksie "
                    + index.toString());
            return null;
        }
        return actors.get(index);
    }

    /**
     * Nadpisuje wybranego aktora w liscie aktorow.
     * @param index indeks aktora, ktory zostanie ndapisany.
     * @param actor nowa nazwa aktora.
     */
    public void setActor(Integer index, String actor) {
        if (index >= getActorsCount())
        {
            logger.warn("setActor(Integer, String) probowal nadpisac nieistniejacy element o indeksie "
                    + index.toString());
            return;
        }
        actors.set(index, actor);
    }

    /**
     * Dodaje aktora na koniec listy aktorow.
     * @param actor Nazwa aktora.
     */
    public void addActor(String actor) { actors.add(actor);
    }

    /**
     * Zwraca rozmiar listy aktorow.
     * @return Rozmiar listy aktorow.
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
    public Visitor acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
