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

//todo: uzgodnic z Product Ownerem, czy ta klasa jest wymagana takze do innych celow niz debugging

/**
 * Wczytuje plik tesktowy i zwraca obiekt typu {@link pl.put.poznan.checker.scenario.Scenario Scenariusz}.
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class ScenarioFileLoader {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioFileLoader.class);
    //Ponizej znajduja sie przelaczniki i slowa kluczowe dotyczace formatu wczytywanego pliku tekstowego

    /**
     * Domyslny konstruktor ScenarioFileLoader.
     */
    public ScenarioFileLoader()
    {

    }
    /**
     * Przelacznik ignorowania bialych znakow, jezeli jest ich za duzo po slowie kluczowym lub
     * po {@link #delim znaku speratora poziomow zaglebien} (albo numeracji).
     * <p>Dla "<i>Tytul:&nbsp;&nbsp;&nbsp;&nbsp;jakas&nbsp;&nbsp;&nbsp;nazwa</i>":<br>
     * Gdy <code>bIgnoreExcessWhitechars = true</code>,&nbsp;&nbsp; wczytany tytul to "<i>jakas&nbsp;&nbsp;&nbsp;nazwa</i>".<br>
     * Gdy <code>bIgnoreExcessWhitechars = false</code>,  wczytany tytul to "<i>&nbsp;&nbsp;&nbsp;jakas&nbsp;&nbsp;&nbsp;nazwa</i>".</p>
     */
    public static Boolean bIgnoreExcessWhitechars = true;
    /**
     * Decyduje, czy kroki na najwyzszym poziomie musza zaczynac sie od znaku separatora poziomow zaglebienia.
     * <p>
     * <code>bStartWithDelim = true</code>:<br>
     * - Pierwszy krok<br>
     * <code>bStartWithDelim = false</code>:<br>
     * Pierwszy krok</p>
     */
    public static Boolean bStartWithDelim = true;
    //todo: wczytywac numeracje zamiast tego, chyba ze wymogiem bedzie wspierac wczytywanie plikow pochodzacych z innych
    //todo: źrodel niz output naszego programu
    /**
     * Znak uzywany do wyznaczenia poziomu zaglebien.
     */
    public static String delim = "-"; //Wyznacza poziom zaglebien
    private static final List<String> keywords = Arrays.asList("FOR EACH", "IF", "ELSE");
    private static final String titleFormat = "Tytuł: ";
    private static final String actorFormat = "Aktorzy: ";
    private static final String systemActorFormat = "Aktor systemowy: ";
    /**
     * Dodatkowy enum, ktory ulatwi rozszerzanie przetwarzania plikow o dodatkowa funkcjonalnosc np. mozliwosc dzielenia
     * listy aktorow na kilku liniach. <br>
     * Na razie zbedny.
     * */
    private enum ReadMode {NONE, TITLE, ACTOR, SYSTEM_ACTOR}
    /**
     * Tworzy obiekt typu <code>{@link pl.put.poznan.checker.scenario.Scenario Scenario}</code> na podstawie pliku tekstowego.
     * @param path plik z ktorego zostanie utworzony scenariusz
     * @return Wypelniony obiekt typu <code>Scenario</code>.
     * @throws IOException blad przetwarzania pliku tekstowego (systemowy)
     * */
    public static Scenario loadScenario(String path) throws IOException
    {
        logger.debug("loadScenario(String) probuje wczytac scenariusz");
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
            logger.error("loadScenario(String): blad odczytu z pliku");
            throw e;
        }

        //Informacje ogolne o scenariuszu
        Integer informacje = 0;

        ReadMode readMode = ReadMode.NONE;
        while (informacje.compareTo(3) != 0) {
            String buf = buffer.readLine();
            //Koniec pliku?
            if (buf == null) {
                logger.error("loadScenario(String) przedwczesny koniec pliku, udalo sie przetworzyc "
                        + informacje.toString() + " slow kluczowych");
                return null;
            }
            if (buf.isBlank())
                continue;
            //Pozbbycie sie bialych znakow
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
        //Podscenariusz, do ktorego bedziemy wpisywac kroki
        SubScenario subScenario = new SubScenario();
        //Na razie jest nim podscenariusz glowny, ale to sie moze zmienic
        ret.setMain(subScenario);

        //Przetwarzanie dzieje sie tutaj
        loadSubScenario(buffer, subScenario, bStartWithDelim ? 1 : 0);

        logger.info("loadScenario(String) pomyslnie wczytano scenariusz \"" + ret.getName() + '\"' );
        return ret;
    }

    /**
     * Tworzy obiekt typu <code>{@link pl.put.poznan.checker.scenario.SubScenario SubScenario}</code> na podstawie czesci tekstu z pliku tekstowego.
     * @param reader - strumien odczytujacy z pliku
     * @return Ostatni <code>{@link pl.put.poznan.checker.scenario.Step krok}, ktory wymaga dodatkowego przetwarzania, poniewaz spowodowal zakonczenie generacji
     * podscenariusza i do niego nie nalezy, wiec trzeba przypisac go tam, gdzie powinien sie znalezc.
     * @throws IOException blad przetwarzania pliku tekstowego (systemowy)
     * */
    private static String loadSubScenario(BufferedReader reader, SubScenario subScenario, Integer subScenarioLevel)
            throws IOException
    {
        logger.debug("loadSubScenario(BufferedReader, SubScenario, Integer) probuje wczytac podscenariusz");
        String step = null;
        boolean bRead = true;
        while (true)
        {
            if (bRead) {
                try {
                    step = reader.readLine();
                } catch (IOException e) {
                    logger.error("loadSubScenario(BufferedReader, SubScenario, Integer) blad odczytu pliku\"");
                    throw e;
                }
            }
            bRead = true; //reset
            //Koniec pliku tekstowego
            //Pozbbycie sie bialych znakow
            if (step == null)
                return null;
            if (step.isBlank())
                continue;
            //Sprawdzamy poziom zaglebienia kroku
            //Dopoki pierwszy znak jest rowny [delim], ucinamy go i zwiekszamy poziom zaglebienia
            String stepText = step;
            Integer level = 0;

            while (Character.isWhitespace(step.charAt(0)))
                step = step.substring(1);

            for (;stepText.indexOf(delim) == 0; ++level)
                stepText = stepText.substring(1);

            if (step.isBlank())
            {
                logger.warn("loadSubScenario(BufferedReader, SubScenario, Integer) pusty krok");
                continue;
            }

            if (bIgnoreExcessWhitechars)
                while (Character.isWhitespace(stepText.charAt(0)))
                    stepText = stepText.substring(1);

            //Jezeli musielibysmy sprawdzic, czy nie wystapilo slowo kluczowe, to mozna zrobic to tak
            //for (String keyword : keywords)
            //    if (step.indexOf(keyword) == 0)
            //    {
            //    }

            //Poziom zaglebienia jest wiekszy niz obecny - trzeba stworzyc podscenariusz
            //Rekurencyjnie wracamy do scenariusza o podanym poziomie
            if (level < subScenarioLevel)
                return step;

            if (level.equals(subScenarioLevel))
                subScenario.addStep(stepText);
                //Tworzymy podscenariusz!
            else
            {
                //Poziom zaglebienia moze byc tylko o jeden wiekszy!
                if ((level - subScenarioLevel) > 1)
                {
                    logger.error("loadSubScenario(BufferedReader, SubScenario, Integer) niepoprawnie zapisany Scenariusz: niekonsystentne " +
                            "poziomy zaglebien. Krok powodujacy blad:\n " + step);
                    return null;
                }
                //Nowy podscenariusz trzeba powiazac z ostatnim krokiem obecnego podscenariusza
                SubScenario nextSubScenario = subScenario.getStep(subScenario.getLength() - 1).getChild();
                if (nextSubScenario == null) {
                    nextSubScenario = new SubScenario();
                    subScenario.getStep(subScenario.getLength() - 1).setChild(nextSubScenario);
                }
                nextSubScenario.addStep(stepText);
                //Zamiast tego mozemy skorzystac z innego rodzaju strumienia do zczytywania danych z pliku
                //PushbackInputStream pozwala "cofnac" ostatni odczyt
                //Tak naprawde z powrotem wpychalibysmy do niego te same znaki, ktore wlasnie odczytalismy
                //Jezeli uwazacie, ze ten zapis jest nieczytelny, to mozna to zmienic
                step = loadSubScenario(reader, nextSubScenario, level);
                bRead = false;
            }
        }
    }
}
