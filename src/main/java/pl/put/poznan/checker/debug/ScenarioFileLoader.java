package pl.put.poznan.checker.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.SubScenario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//todo: uzgodnić z Product Ownerem, czy ta klasa jest wymagana także do innych celów niż debugging

/**
 * Wczytuje plik tesktowy i zwraca obiekt typu Scenariusz
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class ScenarioFileLoader {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioFileLoader.class);
    //Poniżej znajdują się przełączniki i słowa kluczowe dotyczące formatu wczytywanego pliku tekstowego
    /**
     Przełącznik ignorowania białych znaków, jeżeli jest ich za dużo po słowie kluczowym lub
     po znaku sperataora poziomów zagłębień (numeracji) np. dla "Tytuł:    jakaś   nazwa":<br>
     Gdy bIgnoreExcessWhitechars = true,   wczytany tytuł to "jakaś   nazwa"<br>
     Gdy bIgnoreExcessWhitechars = false,  wczytany tytuł to "   jakaś   nazwa"
     */
    public static Boolean bIgnoreExcessWhitechars = true;
    /**
     * Decyduje, czy kroki na najwyższym poziomie muszą zaczynać się od znaku separatora poziomów zagłębienia
     * <br>
     * True:<br>
     * - Pierwszy krok<br>
     * False:<br>
     * Pierwszy krok
     */
    public static Boolean bStartWithDelim = true;
    //todo: wczytywać numerację zamiast tego, chyba że wymogiem będzie wspierać wczytywanie plików pochodzących z innych
    //todo: źródeł niż output naszego programu
    public static String delim = "-"; //Wyznacza poziom zagłębień
    private static final List<String> keywords = Arrays.asList("FOR EACH", "IF", "ELSE");
    private static final String titleFormat = "Tytuł: ";
    private static final String actorFormat = "Aktorzy: ";
    private static final String systemActorFormat = "Aktor systemowy: ";
    /**
     * Dodatkowy enum, który ułatwi rozszerzanie przetwarzania plików o dodatkową funkcjonalność np. możliwość dzielenia
     * listy aktorów na kilku liniach <br>
     * Na razie zbędny
     * */
    private enum ReadMode {NONE, TITLE, ACTOR, SYSTEM_ACTOR}
    /**
     * Tworzy obiekt typu <code>Scenario</code> na podstawie pliku tekstowego
     * @param path plik z którego zostanie utworzony scenariusz
     * @return wypełniony obiekt typu <code>Scenario</code>
     * @throws IOException błąd przetwarzania pliku tekstowego (systemowy)
     * */
    public static Scenario loadScenario(String path) throws IOException
    {
        logger.debug("loadScenario(String path) próbuje wczytać scenariusz");
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
            logger.error("loadScenario(String path): błąd odczytu z pliku");
            throw e;
        }

        //Informacje ogólne o scenariuszu
        Integer informacje = 0;

        ReadMode readMode = ReadMode.NONE;
        while (informacje.compareTo(3) != 0) {
            String buf = buffer.readLine();
            //Koniec pliku?
            if (buf == null) {
                logger.error("loadScenario(String path) przedwczesny koniec pliku, udało się przetworzyć " + informacje.toString() + " słów kluczowych");
                return null;
            }
            if (buf.isBlank())
                continue;
            //Pozbbycie się białych znaków
            while (Character.isWhitespace(buf.charAt(0)))
                buf = buf.substring(1);

            //Identyfikuje co czytamy
            if (buf.indexOf(titleFormat) == 0) {
                readMode = ReadMode.TITLE;
                ++informacje;
            } else if (buf.indexOf(actorFormat) == 0) {
                readMode = ReadMode.ACTOR;
                ++informacje;
            } else if (buf.indexOf(systemActorFormat) == 0) {
                readMode = ReadMode.SYSTEM_ACTOR;
                ++informacje;
            }
            //Odpowiada za przetwarzanie danych
            switch (readMode) {
                case TITLE:
                    if (buf.isBlank())
                        break;

                    if (buf.indexOf(titleFormat) == 0)
                        buf = buf.substring(titleFormat.length());
                    if (bIgnoreExcessWhitechars)
                        while (Character.isWhitespace(buf.charAt(0)))
                            buf = buf.substring(1);
                    ret.setName(((ret.getName() == null) ? "" : ret.getName()) + buf);
                    break;
                case ACTOR:
                    if (buf.isBlank())
                        break;

                    if (buf.indexOf(actorFormat) == 0)
                        buf = buf.substring(actorFormat.length());

                    for (String actor : buf.split(" +")) {
                        if (actor.isBlank())
                            continue;
                        if (bIgnoreExcessWhitechars)
                            while (Character.isWhitespace(actor.charAt(0)))
                                actor = actor.substring(1);
                        ret.addActor(actor);
                    }
                    break;
                case SYSTEM_ACTOR:
                    if (buf.isBlank())
                        break;

                    if (buf.indexOf(systemActorFormat) == 0)
                        buf = buf.substring(systemActorFormat.length());
                    if (bIgnoreExcessWhitechars)
                        while (Character.isWhitespace(buf.charAt(0)))
                            buf = buf.substring(1);
                    ret.setSystemActor(((ret.getSystemActor() == null) ? "" : ret.getSystemActor()) + buf);
                    break;
            }
        }
        //Podscenariusz, do którego będziemy wpisywać kroki
        SubScenario subScenario = new SubScenario();
        //Na razie jest nim podscenariusz główny, ale to się może zmienić
        ret.setMain(subScenario);

        //Przetwarzanie dzieje się tutaj
        loadSubScenario(buffer, subScenario, bStartWithDelim ? 1 : 0);

        logger.debug("loadScenario(String path) pomyślnie wczytano scenariusz \"" + ret.getName() + '\"' );
        return ret;
    }

    /**
     * Tworzy obiekt typu <code>SubScenario</code> na podstawie części tekstu z pliku tekstowego
     * @param reader - strumień odczytujący z pliku
     * @return ostatni krok, który wymaga dodatkowego przetwarzania, ponieważ spowodował zakończenie generacji
     * podscenariusza i do niego nie należy, więc trzeba przypisać go tam, gdzie powinien się znaleźć
     * @throws IOException błąd przetwarzania pliku tekstowego (systemowy)
     * */
    private static String loadSubScenario(BufferedReader reader, SubScenario subScenario, Integer subScenarioLevel)
            throws IOException
    {
        String step = null;
        boolean bRead = true;
        while (true)
        {
            if (bRead) {
                try {
                    step = reader.readLine();
                } catch (IOException e) {
                    logger.error("loadScenario(String path) błąd odczytu pliku\"");
                    throw e;
                }
            }
            bRead = true; //reset
            //Koniec pliku tekstowego
            //Pozbbycie się białych znaków
            if (step == null)
                return null;
            if (step.isBlank())
                continue;
            //Sprawdzamy poziom zagłębienia kroku
            //Dopóki pierwszy znak jest równy [delim], ucinamy go i zwiększamy poziom zagłębienia
            String stepText = step;
            Integer level = 0;

            while (Character.isWhitespace(step.charAt(0)))
                step = step.substring(1);

            for (;stepText.indexOf(delim) == 0; ++level)
                stepText = stepText.substring(1);

            if (step.isBlank())
            {
                logger.warn("loadScenario(String path) pusty krok!");
                continue;
            }

            if (bIgnoreExcessWhitechars)
                while (Character.isWhitespace(stepText.charAt(0)))
                    stepText = stepText.substring(1);

            //Jeżeli musielibyśmy sprawdzić, czy nie wystąpiło słowo kluczowe, to można zrobić to tak
            //for (String keyword : keywords)
            //    if (step.indexOf(keyword) == 0)
            //    {
            //    }

            //Poziom zagłębienia jest większy niż obecny - trzeba stworzyć podscenariusz
            //Rekurencyjnie wracamy do scenariusza o podanym poziomie
            if (level < subScenarioLevel)
                return step;

            if (level.equals(subScenarioLevel))
                subScenario.addStep(stepText);
                //Tworzymy podscenariusz!
            else
            {
                //Poziom zagłębienia może być tylko o jeden większy!
                if ((level - subScenarioLevel) > 1)
                {
                    logger.error("loadScenario(String path) niepoprawnie zapisany plik Scenariusz: niekonsystentne" +
                            "poziomy zagłębień. Krok powodujący błąd:\n " + step);
                    return null;
                }
                //Nowy podscenariusz trzeba powiązać z ostatnim krokiem obecnego podscenariusza
                SubScenario nextSubScenario = subScenario.getStep(subScenario.getLength() - 1).getChild();
                if (nextSubScenario == null) {
                    nextSubScenario = new SubScenario();
                    subScenario.getStep(subScenario.getLength() - 1).setChild(nextSubScenario);
                }
                nextSubScenario.addStep(stepText);
                //Zamiast tego możemy skorzystać z innego rodzaju strumienia do zczytywania danych z pliku
                //PushbackInputStream pozwala "cofnąć" ostatni odczyt
                //Tak naprawdę z powrotem wpychalibyśmy do niego te same znaki, które właśnie odczytaliśmy
                //Jeżeli uważacie, że ten zapis jest nieczytelny, to można to zmienić
                step = loadSubScenario(reader, nextSubScenario, level);
                bRead = false;
            }
        }
    }
}
