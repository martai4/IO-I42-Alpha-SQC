package pl.put.poznan.checker.logic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.SubScenario;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Odpowiada za logikę wczytywania, sprawdzania poprawności i przetwarzania scenariuszy
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class ScenarioQualityChecker
{
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityChecker.class);

    private static final Boolean bIgnoreExcessWhitechars = true; //przełącznik ignorowania białych znaków
    private static final String delim = "-"; //Wyznacza poziom zagłębień
    private static final List<String> keywords = Arrays.asList("FOR EACH", "IF", "ELSE");
    private static final String titleFormat = "Tytuł:";
    private static final String actorFormat = "Aktorzy:";
    private static final String systemActorFormat = "Aktor systemowy:";
    private enum ReadMode {NONE, TITLE, ACTOR, SYSTEM_ACTOR};
    /**
     * Tworzy obiekt typu <code>Scenario</code> na podstawie pliku tekstowego
     * @param path plik z którego zostanie utworzony scenariusz
     * @return wypełniony obiekt typu <code>Scenario</code>
     * */
    public Scenario loadScenario(String path) throws IOException
    {
        logger.debug("[DEBUG] [pl.put.poznan.checker.logic.ScenarioQualityChecker.loadScenario] próbuje wczytać scenariusz");
        Scenario ret = new Scenario();
        FileReader scenarioFile = null;
        BufferedReader buffer = null;
        try
        {
            scenarioFile = new FileReader(path);
            //Pozwala na odczyt linia po linii
            buffer = new BufferedReader(scenarioFile);
        }
        catch (IOException e)
        {
            logger.error("[ERROR] [pl.put.poznan.checker.logic.ScenarioQualityChecker.loadScenario] błąd oczytu z pliku");
            throw e;
        }

        //Informacje ogólne o scenariuszu
        int informacje = 0;

        ReadMode readMode = ReadMode.NONE;
        //todo: dodać dodatkowe pytania:
        //
        //
        //
        while (informacje != 3) {
            String buf = buffer.readLine();
            //Koniec pliku?
            if (buf == null) {
                //todo: warn
                logger.warn("");
                return null;
            }
            //Pozbbycie się białych znaków
            while (Character.isWhitespace(buf.charAt(0)))
                buf = buf.substring(1);

            //Identyfikuje co czytamy
            if (buf.indexOf(titleFormat) == 0) {
                readMode = ReadMode.TITLE;
                ++informacje;
            } else if (buf.equals(actorFormat)) {
                readMode = ReadMode.ACTOR;
                ++informacje;
            } else if (buf.equals(systemActorFormat)) {
                readMode = ReadMode.SYSTEM_ACTOR;
                ++informacje;
            }
            //Odpowiada za przetwarzanie danych
            switch (readMode) {
                case TITLE:
                    String title;
                    if (buf.indexOf(titleFormat) == 0)
                        title = buf.substring(titleFormat.length());
                    else
                        title = buf;
                    if (bIgnoreExcessWhitechars)
                        while (Character.isWhitespace(buf.charAt(0)))
                            title = title.substring(1);
                    ret.setName(ret.getName() + title);
                    break;
                case ACTOR:
                    String actors;
                    if (buf.indexOf(actorFormat) == 0)
                        actors = buf.substring(actorFormat.length());
                    else
                        actors = buf;
                    while (!actors.isBlank()) {
                        int separatorPos = Math.min(actors.indexOf(' '), actors.indexOf('\t')); //todo: sprawdzić, czy '\t' ma w ogóle sens
                        String actor = buf.substring(0, separatorPos);
                        if (bIgnoreExcessWhitechars)
                            while (Character.isWhitespace(buf.charAt(0)))
                                actor = actor.substring(1);
                            ret.addActor(actor);
                        actors = actor.substring(separatorPos + 1);
                    }
                    break;
                case SYSTEM_ACTOR:
                    String systamActor;
                    if (buf.indexOf(systemActorFormat) == 0)
                        systamActor = buf.substring(systemActorFormat.length());
                    else
                        systamActor = buf;
                    if (bIgnoreExcessWhitechars)
                        while (Character.isWhitespace(buf.charAt(0)))
                            systamActor = systamActor.substring(1);
                    ret.setSystemActor(ret.getSystemActor() + systamActor);
                    break;
            }
        }
        //Podscenariusz, do którego będziemy wpisywać kroki
        SubScenario subScenario = new SubScenario();
        //Na razie jest nim podscenariusz główny, ale to się może zmienić
        ret.setMain(subScenario);

        //Przetwarzanie dzieje się tutaj
        loadSubScenario(buffer, subScenario, 0);

        logger.debug("[DEBUG] [pl.put.poznan.checker.logic.ScenarioQualityChecker.loadScenario] pomyślnie wczytano" +
                " scenariusz \"" + ret.getName() + '\"' );
        return ret;
    }

    /**
     * Tworzy obiekt typu <code>SubScenario</code> na podstawie części tekstu z pliku tekstowego
     * @param reader - strumień odczytujący z pliku
     * @return ostatni krok, który wymaga dodatkowego przetwarzania, ponieważ spowodował zakończenie generacji
     * podscenariusza i do niego nie należy, więc trzeba przypisać go tam, gdzie powinien się znaleźć
     * */
    private String loadSubScenario(BufferedReader reader, SubScenario subScenario, Integer subScenarioLevel)
            throws IOException
    {
        while (true)
        {
            String step;
            try
            {
                step = reader.readLine();
            }
            catch (IOException e)
            {
                throw e;
            }
            //Koniec pliku tekstowego
            if (step == null)
                return null;
            //Sprawdzamy poziom zagłębienia kroku
            //Dopóki pierwszy znak jest równy [delimiter], ucinamy go i zwiększamy poziom zagłębienia
            String stepText = step;
            Integer level = 0;
            while (stepText.indexOf(delim) == 0)
            {
                stepText = stepText.substring(stepText.length());
                ++level;
            }

            //Jeżeli musielibyśmy sprawdzić, czy nie wystąpiło słowo kluczowe, to można zrobić to tak
            //for (String keyword : keywords)
            //    if (step.indexOf(keyword) == 0)
            //    {
            //    }

            //Poziom zagłębienia jest większy niż obecny - trzeba stworzyć podscenariusz
            //Rekurencyjnie wracamy do scenariusza o podanym poziomie
            if (level < subScenarioLevel)
                return step;
                //Tworzymy podscenariusz!
            if (level.equals(subScenarioLevel))
                subScenario.addStep(stepText);
            else
            {
                //Poziom zagłębienia może być tylko o jeden większy!
                if ((level - subScenarioLevel) > 1)
                {
                    // throw ;
                }
                //Nowy podscenariusz trzeba powiązać z ostatnim krokiem obecnego podscenariusza
                SubScenario nextSubScenario = new SubScenario();
                subScenario.getStep(subScenario.getLength() - 1).setChild(nextSubScenario);

                //Zamiast tego możemy skorzystać z innego rodzaju strumienia do zczytywania danych z pliku
                //PushbackInputStream pozwala "cofnąć" ostatni odczyt
                //Tak naprawdę z powrotem wpychalibyśmy do niego te same znaki, które właśnie odczytaliśmy
                //Jeżeli uważacie, że ten zapis jest nieczytelny, to można to zmienić
                String unprocessedStep = loadSubScenario(reader, nextSubScenario, level);
                if (unprocessedStep == null)
                    return null;

                //Powtarzamy zbieranie poziomu kroku
                //Tym razem tego kroku, które zwróciło rekurencyjne wywołanie loadSubScenario
                String unprocessedStepText = unprocessedStep;
                int unprocessedLevel = 0;
                while (unprocessedStepText.indexOf(delim) == 0)
                {
                    unprocessedStepText = unprocessedStepText.substring(unprocessedStepText.length());
                    ++unprocessedLevel;
                }
                //Teraz mamy pewność, że poziom unprocessedLevel jest <= poziomu obecnie przetwarzanego podscenariusza
                //Inaczej stworzyłby się kolejny podscenariusz w rekurencyjnym wywołaniu loadSubScenario i ono zwróciło
                //by nam krok null albo o poziomie niższym niż subScenarioLevel
                //Musimy sprawdzić, czy poziomy są sobie równe, wtedy krok trzeba dodać do obecnego podscenariusza
                //Jeżeli jest mniejszy, to trzeba go dodać do podscenariusza wyżej
                if (unprocessedLevel < subScenarioLevel)
                {
                    return unprocessedStepText;
                }
                else
                {
                    subScenario.addStep(unprocessedStepText);
                }
            }
        }
    }
}
