package pl.put.poznan.checker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.ScenarioQualityChecker;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.ScenarioRepository;

import java.util.HashMap;
import java.util.List;

//todo: logging oraz testy mockiem
/**
 * Obsługuje dostęp do wymaganych funkcjonalności porzez <b>REST API</b>. Na podstawie odpowiednich linków i ich
 * parametrów wywołuje metody odpowiadające danym funkcjonalnościom i zwraca uzyskane wyniki.
 *
 * @author I42-Alpha
 * @version 2.0
 */
@RestController
@RequestMapping("/scenarios")
public class ScenariosController
{
    private static final Logger logger = LoggerFactory.getLogger(ScenariosController.class);
    /**
     * Struktura do przechowywania przesłanych {@link Scenario Scenariuszy}.
     */
    private final ScenarioRepository scenarioRepository = new ScenarioRepository();

    /**
     * Domyślny konstruktor <code>ScenariosController</code>.
     */
    ScenariosController(){}
  
    /**
     * Umożliwia dodanie do {@link ScenarioRepository Repozytorium} {@link Scenario kolejnego Scenariusza}.<br>
     * Używany URL: ip_hosta/
     *
     * @param scenario <i>Scenariusz</i> pobrany z zapytania, przesłany w formacie <b>JSON</b> i przetworzony na strukturę używaną
     * w programie
     */
    @PostMapping("/")
    public void addScenario(@RequestBody Scenario scenario)
    {
        if (scenario == null)
        {
            //logger.error
            return;
        }
      
        logger.info("Zdalne API próbuje dodać Scenariusz \"{}\"", scenario.getName());
        scenarioRepository.addScenario(scenario);
    }

    /**
     * Metoda pozwala pobrać z {@link ScenarioRepository Repozytorium} {@link Scenario Scenariusz} o wskazanym <b>ID</b> w formacie <b>JSON</b>.<br>
     * Uzywany url: ip_hosta/{ID}
     *
     * @param id <b>ID</b> <i>Scenariusza</i>, który użytkownik chce pobrać
     * @return <b>Odpowiedź HTTP</b>, w której ciele znajduje się <b>JSON</b> z zadanym <i>Scenariuszem</i> lub <b>kodem błędu</b> 404, jeśli nie ma
     * <i>Scenariusza</i> o takim <b>ID</b>.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getScenario(@PathVariable("id") Integer id) {
        var scenario = scenarioRepository.getScenario(id);
        if (scenario == null)
        {
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o numerze {}", id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            logger.info("Zgdalne API zwraca Scenariusz o numerze {} i nazwie \"{}\"",
                        id.toString(), scenario.getName());
            return ResponseEntity.ok(scenario);
        }
    }

    /**
     * Zwraca listę wszystkich {@link Scenario Scenariuszy} w {@link ScenarioRepository Repozytorium} jako pary <code>[id, tytul]</code>.<br>
     * Używany URL: ip_hosta/listAll
     *
     * @return <b>Odpowiedź HTTP</b>, w której ciele znajduje się <b>lista par</b> w postaci <b>JSON</b>.
     */
    @GetMapping("/listAll")
    public ResponseEntity<HashMap<Integer, String>> listAllScenarios()
    {
        /*HashMap<Integer, String> scenarios = new HashMap<>();
        for (var key : scenarioRepository.getScenarios().keySet()) {
            scenarios.put(key, scenarioRepository.getScenario(key).getName());
        }*/
        HashMap<Integer, String> scenarios = new HashMap<>(); //todo: nie lepiej lista?
        int i = 0;
        for (var scenario : scenarioRepository.getScenarios())
            scenarios.put(i++, scenario.getName());

        return ResponseEntity.ok(scenarios);
    }

    /**
     * Zwraca z {@link ScenarioRepository Repozytorium} {@link Scenario Scenariusz} o wskazanej <b>nazwie</b>. <b style="color:red;">UWAGA!: jeśli w repozytorium znajduje się więcej
     * <i>Scenariuszy</i> o tej samej nazwie, zwrócony zostanie pierwszy znaleziony. Aby uzyskać dostęp do pozostałych
     * należy odwołać się do nich przez</b> {@link ScenariosController#getScenario(Integer) <b>ID</b>}. <br>
     * Uzżwany URL: ip_hosta/byName/{name}
     *
     * @param name Tytuł <i>Scenariusza</i>, który chcemy uzyskać
     * @return Odpowiedź z poszukiwanym <i>Scenariuszem</i> w postaci <b>JSON</b> lub <b>kod błędu</b> 404, jeśli nie ma w {@link ScenarioRepository Repozytorium} <i>Scenariusza</i> o takiej nazwie.
     */
    @GetMapping("/byName/{name}")
    public ResponseEntity<Scenario> getScenarioByName(@PathVariable("name") String name)
    {
        Scenario scenario = scenarioRepository.getScenarioByName(name);
        if (scenario == null)
        {
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o nazwie \"{}\"", name);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(scenario);
    }

    /**
     * Zwraca {@link Scenario Scenariusz} jako <b>tekst</b>, z <b>ponumerowanymi poziomami zagłębien</b>.<br>
     * Używany URL: ip_hosta/text/{id}
     *
     * @param id <b>ID</b> <i>scenariusza</i>, który ma zostać przetworzony
     * @return Odpowiedź <b>HTTP</b>, w którj ciele znajduje się przetworzony <i>Scenariusz</i> w postaci <b>JSON</b> lub kod błędu 404 w razie
     * braku <i>Scenariusza</i> o takim <b>ID</b>.
     */
    @GetMapping("/text/{id}")
    public ResponseEntity<String> getTextifiedScenario(@PathVariable("id") Integer id)
    {
        var scenario = scenarioRepository.getScenario(id);
        if (scenario == null)
        {
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o numerze {}", id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ScenarioQualityChecker.getScenarioTextified(scenario));
    }

    /**
     * Zwraca wskazany {@link Scenario Scenariusz} pod <b>nową nazwą</b> i zapisuje przetworzony <i>Scenariusz</i> w
     * {@link ScenarioRepository Repozytorium}. Pozwala na zmianę <i>Scenariusza</i> tak, aby zawierał {@link SubScenario PodScenariusze} tylko do <b>określonego poziomu</b>.<br>
     * Używany URL: ip_hosta/toLevel/ {id} {lvl} {name}
     *
     * @param id    <b>ID</b> <i>Scenariusza</i>, który ma zostać przetworzony
     * @param level <b>Maksyamalny poziom zagłębienia</b>, który ma zawierać przetworzony <i>Scenariusz</i>; musi być większy od 1
     * @param name  <b>Nazwa</b>, pod która ma zostać zapisany przetworzony <i>Scenariusz</i>
     * @return Odpowiedź <b>HTTP</b>, w której ciele znajduje się <b>JSON</b> z przetworzonym <i>Scenariuszem</i> lub <b>kod błędu</b> 404, jeśli nie
     * znaleziono <i>Scenariusza</i> o wskazanym <b>ID</b>.
     */
    @GetMapping("/toLevel/{id} {lvl} {name}")
    public ResponseEntity<Scenario> getScenarioToLevel(@PathVariable("id") Integer id, @PathVariable("lvl") int level, @PathVariable("name") String name)
    {
        Scenario toConvert = scenarioRepository.getScenario(id);
        if (toConvert == null)
        {
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o numerze {}", id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Scenario converted;
        try
        {
            converted = ScenarioQualityChecker.getScenarioUpToLevel(toConvert, level);
        }
        catch (Exception ex)
        {
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o numerze {}", id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        scenarioRepository.addScenario(converted);//to na pewno tak ma być? todo:check
        return ResponseEntity.ok(converted);
    }

    /**SubScenario
     * Pozwala sprawdzić, ile {@link Step Kroków} {@link Scenario Scenariusza} zaczyna się od <b>słów kluczowych</b>.<br>
     * Używany URL: ip_hosta/keywordsNumber/{id}
     *
     * @param id <b>ID</b> <i>Scenariusza</i> do przetworzenia
     * @return Odpowiedź <b>HTTP</b> z <b>JSON</b>'em o liczbie <i>Kroków</i> zaczynających się od <b>słów kluczowych</b> lub <b>kod błędu></b> 404, jeśli nie
     * znaleziono <i>Scenariusza</i> o wskazanym <b>ID</b>.
     */
    @GetMapping("/keywordsNumber/{id}")
    public ResponseEntity<Integer> getScenarioKeywordsNumber(@PathVariable("id") Integer id)
    {
        Scenario scenario = scenarioRepository.getScenario(id);
        if (scenario == null)
        {
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o numerze {}", id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ScenarioQualityChecker.getDecisionCount(scenario));
    }

    /**
     * Pozwala sprawdzić, które {@link Step Kroki} {@link Scenario Scenariusza} nie zaczynają się od <b>aktora</b> lub <b>aktora systemowego</b>.<br>
     * Używany URL: ip_hosta/noActorsStep/{id}
     *
     * @param id <b>ID</b> <i>Scenariusza</i> do przetworzenia
     * @return Odpowiedz <b>HTTP</b> z <b>JSON</b>'em, który zawiera listę <i>Krokow</i>, nie zaczynających się od <b>aktorów</b> lub <b>aktora systemowego</b> albo 
     * <b>kod błędu</b> 404, jeśli nie znaleziono <i>Scenariusza</i> o wskazanym <b>ID</b>`.
     */
    @GetMapping("/noActorsStep/{id}")
    public ResponseEntity<List<String>> getScenarioStepsWithoutActors(@PathVariable("id") Integer id)
    {
        Scenario scenario = scenarioRepository.getScenario(id);
        if (scenario == null)
        {
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o numerze {}", id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ScenarioQualityChecker.getActorErrors(scenario));
    }

    /**
     * Funkcja pozwala sprawdzić, ile {@link Step Kroków} zawiera cały {@link Scenario Scenariusza} (łącznie z {@link ScenariSubScenario PodScenariuszami}).<br>
     * żywany URL: ip_hosta/length/{id}
     *
     * @param id <b>ID</b> <i>Scenariusza</i> do sprawdzenia
     * @return Odpowiedź <b>HTTP</b> z <b>JSON</b>'em o liczbie {@link Step Kroków} w całym {@link Scenario Scenariuszu}.
     */
    @GetMapping("/length/{id}")
    public ResponseEntity<Integer> getScenarioLength(@PathVariable("id") Integer id)
    {
        Scenario scenario = scenarioRepository.getScenario(id);
        if (scenario == null)
        {;
            logger.warn("Zdalne API próbowało zwrócić nieistniejący Scenariusz o numerze {}", id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ScenarioQualityChecker.getScenarioSize(scenario));
    }
}