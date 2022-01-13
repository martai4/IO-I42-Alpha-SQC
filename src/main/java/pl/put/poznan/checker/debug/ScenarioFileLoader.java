package pl.put.poznan.checker.debug;
import pl.put.poznan.checker.logic.ScenarioFormat;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.SubScenario;
import pl.put.poznan.checker.scenario.Step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Wczytuje plik tesktowy i zwraca obiekt typu <code>{@link Scenario Scenariusz}</code>.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class ScenarioFileLoader
{
    private static final Logger logger = LoggerFactory.getLogger(ScenarioFileLoader.class);

    /**
     * Domyślny konstruktor <code>ScenarioFileLoader</code>.
     */
    public ScenarioFileLoader() {}

    /**
     * Dodatkowy <code>enum</code>, który ułatwi rozszerzanie przetwarzania plików o <b>dodatkową funkcjonalność</b> np.
     * możliwość dzielenia listy aktorów na kilku liniach. <br>
     * Na razie zbędny.
     * */
    private enum ReadMode {NONE, TITLE, ACTOR, SYSTEM_ACTOR}

    /**
     * Tworzy {@link Scenario Scenariusz} na podstawie <b>pliku tekstowego</b>.
     * @param stream strumień, z którego zostanie utworzony <i>Scenariusz</i>
     * @return Wypełniony obiekt typu <i>Scenario</i>.
     * */
    public static Scenario loadScenario(BufferedReader stream) throws IOException
    {
        logger.debug("Próba wczytania Scenariusza");
        Scenario ret = new Scenario();

        ReadMode readMode = ReadMode.NONE;

        //Potrzebujemy najpierw wczytać 3 informacje ogólne o Scenariuszu: tytuł, listę aktorów i aktora systemowego
        Integer informacje = 0;
        while (informacje.compareTo(3) != 0)
        {
            String buf = stream.readLine();
            //Koniec pliku?
            if (buf == null)
            {
                logger.error("Przedwczesny koniec pliku, udało się przetworzyć {} słów kluczowych", informacje.toString());

                return null;
            }

            if (buf.indexOf("\uFEFF") == 0)
                buf = buf.substring(1);
            if (buf.isBlank())
                continue;

            //Pozbycie się białych znaków na początku linii
            while (Character.isWhitespace(buf.charAt(0)))
                buf = buf.substring(1);

            //Identyfikujemy, co czytamy
            if (buf.indexOf(ScenarioFormat.titleFormat) == 0)
            {
                readMode = ReadMode.TITLE;
                ++informacje;
            }
            else if (buf.indexOf(ScenarioFormat.actorFormat) == 0)
            {
                readMode = ReadMode.ACTOR;
                ++informacje;
            }
            else if (buf.indexOf(ScenarioFormat.systemActorFormat) == 0)
            {
                readMode = ReadMode.SYSTEM_ACTOR;
                ++informacje;
            }
            //Ustawiamy w Scenariuszu przeczytaną informację
            switch (readMode)
            {
                case TITLE:
                    if (buf.isBlank())
                        break;

                    if (buf.indexOf(ScenarioFormat.titleFormat) == 0)
                        buf = buf.substring(ScenarioFormat.titleFormat.length());

                    if (ScenarioFormat.bIgnoreExcessWhitechars)
                        while (Character.isWhitespace(buf.charAt(0)))
                            buf = buf.substring(1);

                    ret.setName(((ret.getName() == null) ? "" : ret.getName()) + buf);
                    logger.debug("Wczytano tytuł Scenariusza \"{}\"", ret.getName());
                    break;

                case ACTOR:
                    if (buf.isBlank())
                        break;

                    if (buf.indexOf(ScenarioFormat.actorFormat) == 0)
                        buf = buf.substring(ScenarioFormat.actorFormat.length());

                    for (String actor : buf.split(" +"))
                    {
                        if (actor.isBlank())
                            continue;

                        if (ScenarioFormat.bIgnoreExcessWhitechars)
                            while (Character.isWhitespace(actor.charAt(0)))
                                actor = actor.substring(1);
                        ret.addActor(actor);
                        logger.debug("Wczytano aktora \"{}\"", actor);
                    }
                    break;

                case SYSTEM_ACTOR:
                    if (buf.isBlank())
                        break;

                    if (buf.indexOf(ScenarioFormat.systemActorFormat) == 0)
                        buf = buf.substring(ScenarioFormat.systemActorFormat.length());

                    if (ScenarioFormat.bIgnoreExcessWhitechars)
                        while (Character.isWhitespace(buf.charAt(0)))
                            buf = buf.substring(1);

                    ret.setSystemActor(((ret.getSystemActor() == null) ? "" : ret.getSystemActor()) + buf);
                    logger.debug("Wczytano aktora systemowego \"{}\"", ret.getSystemActor());
                    break;
            }
        }
        //PodScenariusz główny, do którego będziemy wpisywać Kroki
        SubScenario subScenario = new SubScenario();
        ret.setMain(subScenario);

        //Tworzy wszystkie Kroki wraz z PodScenariuszami (rekurencyjne wywołania)
        loadSubScenario(stream, subScenario, ScenarioFormat.bStartWithDelim ? 1 : 0);

        logger.info("Pomyślnie wczytano Scenariusz \"{}\"", ret.getName());
        return ret;
    }

    /**
     * Tworzy {@link SubScenario PodScenariusz} na podstawie <b>części tekstu z pliku</b>.
     * @param reader strumień odczytujący znaki z pliku
     * @return Ostatni {@link Step Krok}, który wymaga dodatkowego przetwarzania, ponieważ spowodował zakończenie generacji
     * <i>PodScenariusza</i> i do niego nie należy, więc trzeba przypisać go tam, gdzie powinien się znaleźć.
     * @throws IOException błąd przetwarzania pliku tekstowego
     * */
    private static String loadSubScenario(BufferedReader reader, SubScenario subScenario, Integer subScenarioLevel)
            throws IOException
    {
        logger.debug("Próba wczytania PodScenariusza");
        String step = null;
        //[bRead] sprawdza, czy musimy czytać kolejny Krok. Jeżeli wracamy z rekurencyjnego [loadSubScenario], to został już
        //wczytany do [step] Krok i musimy go przetworzyć zanim wczytamy kolejny
        boolean bRead = true;
        while (true)
        {
            if (bRead)
            {
                try
                {
                    step = reader.readLine();
                }
                catch (IOException e)
                {
                    logger.error("Błąd odczytu");
                    throw e;
                }
            }
            bRead = true; //reset

            //Koniec pliku tekstowego
            if (step == null)
                return null;

            if (step.isBlank())
                continue;

            //Sprawdzamy poziom zagłębienia Kroku
            //Dopóki pierwszy znak jest równy [delim], ucinamy go i zwiększamy poziom zagłębienia
            String stepText = step;
            Integer level = 0;

            //Pozbycie się białych znaków
            while (Character.isWhitespace(step.charAt(0)))
                step = step.substring(1);

            if (ScenarioFormat.bEnumerate)
            {
                if (stepText.contains(".")) {
                    boolean bAllNumerical = true;
                    String num = stepText.substring(0, stepText.indexOf("."));
                    while (bAllNumerical) {
                        for (int i = 0; i != num.length(); ++i)
                            if (!Character.isDigit(num.charAt(i))) {
                                bAllNumerical = false;
                                break;
                            }

                        if (bAllNumerical) {
                            stepText = stepText.substring(stepText.indexOf(".") + 1);
                            ++level;
                            if (!stepText.contains("."))
                                break;
                            num = stepText.substring(0, stepText.indexOf("."));
                        }
                    }
                }
            }
            else
                for (;stepText.indexOf(ScenarioFormat.delim) == 0; ++level)
                    stepText = stepText.substring(1);

            if (stepText.isBlank())
            {
                logger.warn("Pusty Krok");
                continue;
            }

            if (ScenarioFormat.bIgnoreExcessWhitechars)
                while (Character.isWhitespace(stepText.charAt(0)))
                    stepText = stepText.substring(1);

            //Rekurencyjnie wracamy do Scenariusza o podanym poziomie
            if (level < subScenarioLevel)
                return step;

            //Dodajemy Krok
            if (level.equals(subScenarioLevel))
                subScenario.addStep(stepText);
            //Poziom zagłębienia jest większy niz obecny - trzeba stworzyć PodScenariusz
            else
            {
                //Poziom zagłębienia powinien być tylko o jeden większy!
                if ((level - subScenarioLevel) > 1)
                {
                    logger.error("Niepoprawnie zapisany Scenariusz: niekonsystentne " +
                            "poziomy zagłębień. Krok powodujący błąd: {}", step);
                    throw new IOException("badDelim");
                }

                //Nowy PodScenariusz trzeba powiązać z ostatnim Krokiem obecnego PodScenariusza
                if (subScenario.getLength() == 0)
                {
                    logger.error("Niepoprawnie zapisany Scenariusz: pierwszy " +
                            "krok posiada niepoprawny poziom zagłębień. Krok powodujący błąd: {}", step);
                    throw new IOException("badDelim");
                }
                SubScenario nextSubScenario = subScenario.getStep(subScenario.getLength() - 1).getChild();
                if (nextSubScenario == null)
                {
                    nextSubScenario = new SubScenario();
                    subScenario.getStep(subScenario.getLength() - 1).setChild(nextSubScenario);
                }
                nextSubScenario.addStep(stepText);
                step = loadSubScenario(reader, nextSubScenario, level);
                bRead = false;
            }
        }
    }
}
