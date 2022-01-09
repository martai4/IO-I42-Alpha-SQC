package pl.put.poznan.checker.logic.visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.ScenarioFormat;
import pl.put.poznan.checker.logic.visitor.base.Visitor;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Visitor Wizytator} odpowiedzialny za zwracanie <b>błędów</b> powiązanych ze złym użyciem <b>aktorów</b>
 * w {@link Step Krokach} {@link Scenario Scenariusza}.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class ActorVisitor implements Visitor
{
    private static final Logger logger = LoggerFactory.getLogger(ActorVisitor.class);
    private final List<String> errors = new ArrayList<>();
    private List<String> actors = null;
    private String sysActor = null;

    /**
     * Konstruktor wczytujący wszystkie dane.
     * @param actors lista <b>aktorów</b>
     * @param sysActor <b>aktor systemowy</b>
     */
    public ActorVisitor(List<String> actors, String sysActor) { this.actors = actors; this.sysActor = sysActor; }

    /**
     * Sprawdza, czy podany <b>tekst</b> zaczyna się od <b>aktora</b> lub <b>słowa kluczowego</b> z <b>aktorem</b> np.:
     * <p><i><b>System</b> zwraca informację</i> - <code>true</code><br>
     * <i>Informacja jest zwracana przez <b>system</b></i> - <code>false</code></p>
     * @param stepText badany tekst
     * @return <code>true</code>, gdy podany <b>tekst</b> zaczyna się od <b>aktora</b> lub
     * <b>słowa kluczowego</b> z <b>aktorem</b>, <code>false</code> w przeciwnym wypadku
     */
    private boolean isActor(String stepText)
    {
        if (stepText.isBlank()) {
            logger.info("Krok  jest pusty");
            return true;
        }

        String stepTextOriginal = stepText;
        if (stepTextOriginal == null){
            logger.warn("Krok nie zawiera tekstu");
        }
        //Pozbywamy się *białych znaków* na początku *tekstu*, jeżeli ich ignorowanie zostało ustawione
        if (ScenarioFormat.bIgnoreExcessWhitechars) {
            while (Character.isWhitespace(stepText.charAt(0))) {
                stepText = stepText.substring(1);
                logger.info("Usunieto biały znak z początku kroku");
            }
        }
        //Usuwamy *słowo kluczowe*
        for (String key : ScenarioFormat.keys)
            if (stepText.indexOf(key) == 0)
            {
                stepText = stepText.substring(key.length());
                logger.info("Usunieto slowo kluczowe z poczatku kroku {}",key);
                break;
            }

        //Pozbywamy się nadmiaru *białych znaków* po *słowie kluczowym*, jeżeli ich ignorowanie zostało ustawione
        if (ScenarioFormat.bIgnoreExcessWhitechars) {
            while (Character.isWhitespace(stepText.charAt(0))) {
                stepText = stepText.substring(1);
                logger.info("Usunieto biały znak z nowego początku kroku");
            }
        }

        //Jeżeli nasz *zmodyfikowany tekst* zaczyna się od *aktora*, to zwracamy [true]
        for (String actor : actors)
            if (stepText.indexOf(actor) == 0) {
                logger.info("Krok zaczyna się od aktora");
                return true;
            }

        //Zwracamy [true], jeżeli *zmodyfikowany tekst* zaczyna się od *aktora systemowego* lub [false] w przeciwnym wypadku
        boolean ret = stepText.indexOf(sysActor) == 0;
        if (!ret) {logger.warn("Krok \"{}\" zawiera błąd. Brak lub niepoprawny aktor na początku kroku", stepTextOriginal);}
        else{
            logger.info("Krok rozpoczyna się od aktora systemowego");
        }
        return ret;
    }

    /**
     * Odwiedza {@link SubScenario PodScenariusz} główny {@link Scenario Scenariusza}, którego <b>aktorów</b> sprawdzamy.
     *
     * @param scenario {@link Scenario Scenariusz}, który ma odwiedzić <i>Wizytator</i>
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(Scenario scenario)
    {
        if (scenario == null)
        {
            logger.warn("Próbowano odwiedzić Scenariusz, który nie istnieje");
            return this;
        }
        logger.debug("Rozpoczęto przetwarzanie Scenariusza {}", scenario.getName());
        Visitor visit = visit(scenario.getMain());
        logger.debug("Zakończono przetwarzanie Scenariusza {}", scenario.getName());
        return visit;
    }

    /**
     * Odwiedza wszystkie {@link Step Kroki} {@link SubScenario PodScenariusza}.
     *
     * @param subScenario {@link SubScenario PodScenariusz}, który ma odwiedzić <i>Wizytator</i>
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(SubScenario subScenario)
    {
        if (subScenario == null)
        {
            logger.warn("Próbowano odwiedzić PodScenariusz, który nie istnieje");
            return this;
        }

        for (Step s : subScenario.getSteps())
            visit(s);

        return this;
    }

    /**
     * Odwiedza {@link Step Krok} i sprawdza, czy zaczyna się od <b>aktora</b>.
     *
     * @param step {@link Step Krok}, który ma odwiedzić <i>Wizytator</i>
     * @return <i>Wizytator</i> (siebie).
     */
    @Override
    public Visitor visit(Step step)
    {
        //Dodaje aktora do listy błędów, jeżeli jest błędny
        String stepText = step.getText();
        if (!isActor(stepText))
            errors.add(stepText);

        //Odwiedza PodScenariusz
        SubScenario subScenario = step.getChild();
        if (subScenario != null)
            return visit(subScenario);
        return this;
    }

    public List<String> getErrors() { return errors; }
}
