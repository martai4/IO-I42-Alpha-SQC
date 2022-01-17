package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;

import java.util.Arrays;
import java.util.List;

/**
 * Opisuje sposób zapisu i odczytu {@link Scenario Scenariuszy} jako <b>pliki tekstowe</b>.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class ScenarioFormat
{
    /**
     * Domyślny konstruktor <code>ScenarioFormat</code>.
     */
    ScenarioFormat() {}

    /**
     * Dozwolone <b>słowa kluczowe</b>.
     */
    public static List<String> keys = Arrays.asList("IF", "ELSE", "FOR EACH");

    /**
     * Przełącznik ignorowania <b>białych znaków</b>, jeżeli jest ich za dużo po <b>słowie kluczowym</b> lub
     * po <b>{@link #delim znaku speratora poziomów zagłębień}</b> (albo <b>numeracji</b>).
     * <p>Dla "<i>Tytuł:&nbsp;&nbsp;&nbsp;&nbsp;jakaś&nbsp;&nbsp;&nbsp;nazwa</i>":<br>
     * Gdy <code>bIgnoreExcessWhitechars = true</code>,&nbsp;&nbsp; wczytany tytuł to "<i>jakaś&nbsp;&nbsp;&nbsp;nazwa</i>".<br>
     * Gdy <code>bIgnoreExcessWhitechars = false</code>, wczytany tytuł to "<i>&nbsp;&nbsp;&nbsp;jakaś&nbsp;&nbsp;&nbsp;nazwa</i>".</p>
     */
    public static Boolean bIgnoreExcessWhitechars = true;

    /**
     * Decyduje, czy {@link Step Kroki} na <u>najwyższym</u> poziomie muszą zaczynać się od <b>{@link #delim znaku speratora poziomów zagłębień}</b>.
     * Ignorowane, jeżeli wczytywana jest <b>numeracja</b>.
     * <p>
     * <code>bStartWithDelim = true</code>:<br>
     * - Pierwszy krok<br>
     * <code>bStartWithDelim = false</code>:<br>
     * Pierwszy krok</p>
     * Domyślnie <code>true</code>
     */
    public static Boolean bStartWithDelim = true;

    /**
     * Zamiast zczytywania po <b>{@link #delim znaku speratora poziomów zagłębień}</b>, zczytujemy przy pomocy numeracji.
     */
    public static Boolean bEnumerate = true;

    /**
     * Znak używany do wyznaczenia <b>poziomu zagłębień</b>, ale tylko wtedy, gdy <b>numeracja</b> nie występuje.
     */
    public static String delim = "-";

    /**
     * Znaki poprzedzające <b>tytuł</b> {@link Scenario Scenariusza}.
     */
    public static final String titleFormat = "Tytuł: ";
    /**
     * Znaki poprzedzające listę <b>aktorów</b> {@link Scenario Scenariusza}.
     */
    public static final String actorFormat = "Aktorzy: ";
    /**
     * Znaki poprzedzające <b>aktora systemowego</b> {@link Scenario Scenariusza}.
     */
    public static final String systemActorFormat = "Aktor systemowy: ";
}
