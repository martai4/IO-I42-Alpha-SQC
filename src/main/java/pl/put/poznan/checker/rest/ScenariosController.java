package pl.put.poznan.checker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.visitor.LengthVisitor;
import pl.put.poznan.checker.logic.visitor.ScenarioTextifier;
import pl.put.poznan.checker.logic.visitor.SubLevelsVisitor;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.ScenarioRepository;

import java.util.HashMap;
import java.util.List;

/**
 * Klasa obslugujaca dostep do wymaganych funkcjonalnosci porzez REST API. Na podstawie odpowiednich linkow i ich
 * parametrow wywoluje metody odpowiadajace danym funkcjonalnosciom i zwraca uzyskane wyniki.
 *
 * @author I42-Alpha
 * @version 1.0
 */
@RestController
@RequestMapping("/scenarios")
public class ScenariosController {
    private static final Logger logger = LoggerFactory.getLogger(ScenariosController.class);
    /**
     * Struktura do przechowywania przeslanych scenariuszy.
     */
    ScenarioRepository scenarioRepository = new ScenarioRepository();

    /**
     * Metoda umozliwia dodanie do repozytorium scenariuszy kolejnego scenariusza.
     * Uzywany url: ip_hosta/
     *
     * @param scenario Scenariusz pobrany z zapytania, przeslany w formacie JSON i przetworzony na strukture uzywana
     *                 w programie.
     */
    @PostMapping("/")
    public void addScenario(@RequestBody Scenario scenario) {
        logger.info("addScenario(Scenario scenario) próbuje dodać scenariusz: " + scenario.getName());
        scenarioRepository.addScenario(scenario);
    }

    /**
     * Metoda pozwala pobrać z repozytorium scenariusz o wskazanym ID w formacie JSON
     * Uzywany url: ip_hosta/{ID}
     *
     * @param id ID scenariusza, ktory uzytkownik chce pobrac
     * @return Odpowiedz HTTP, w ktorej ciele znajduje JSON z zadanym scenariuszem lub blad 404 jesli nie ma
     * scenariusza o takim ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getScenario(@PathVariable("id") Integer id) {
        var scenario = scenarioRepository.getScenario(id);

        if (scenario == null) {
            logger.warn("getScenario(Integer id) próbował zwrócić nieistniejący scenariusz o numerze " + id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info("getScenario(Integer id) zwraca scenariusz o numerze " + id.toString() + " i nazwie \""
                    + scenario.getName() + "\"");
            return ResponseEntity.ok(scenario);
        }
    }

    /**
     * Metoda zwraca liste wszystkich scenariuszy w repozytorium jako pary {id, tytul}
     * Uzywany url: ip_hosta/listAll
     *
     * @return Odpowiedz HTTP, w ktorej ciele znajduje sie lista par w postaci JSON
     */
    @GetMapping("/listAll")
    public ResponseEntity<HashMap<Integer, String>> listAllScenarios() {
        /*HashMap<Integer, String> scenarios = new HashMap<>();
        for (var key : scenarioRepository.getScenarios().keySet()) {
            scenarios.put(key, scenarioRepository.getScenario(key).getName());
        }*/
        HashMap<Integer, String> scenarios = new HashMap<>(); //todo: nie lepiej lista?
        int i = 0;
        for (var scenario : scenarioRepository.getScenarios()) {
            scenarios.put(i++, scenario.getName());
        }

        return ResponseEntity.ok(scenarios);
    }

    /**
     * Funkcja zwraca z repozytorium scenariusz o wskazanej nazwie. UWAGA: jesli w repozytorium znajduja sie wiecej
     * scenariuszy o tej samej nazwie, zwrocony zostanie pierwszy znaleziony. Aby uzyskac dostep do pozostalych
     * nalezy odwolac sie do nich przez ich ID {@link ScenariosController#listAllScenarios()}
     * {@link ScenariosController#getScenario(Integer)}
     * Uzywany url: ip_hosta/byName/{name}
     *
     * @param name Tytul scenariusza, ktory chcemy uzyskac
     * @return Odpowiedz z poszukiwanym scenariuszem w postaci JSON lub 404 jesli nie ma w repozyterium scenariusza o
     * takiej nazwie
     */
    @GetMapping("/byName/{name}")
    public ResponseEntity<Scenario> getScenarioByName(@PathVariable("name") String name) {
        Scenario act = scenarioRepository.getScenarioByName(name);
        if (act == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(act);
    }

    /**
     * Funkcja zwraca scenariusz jako tekst, z ponumerowanymi poziomami zaglebien.
     * Uzywany url: ip_hosta/text/{id}
     *
     * @param id ID scenariusza, ktory ma zostac przetworzony
     * @return Odpwiedz HTTP, w kterj ciele znajduje sie przetworzony scenariusz w postaci JSON lub blad 404 w razie
     * braku scenariusza o takim ID
     */
    @GetMapping("/text/{id}")
    public ResponseEntity<String> getTextifiedScenario(@PathVariable("id") Integer id) {
        var scenario = scenarioRepository.getScenario(id);
        var textifier = new ScenarioTextifier();
        if (scenario == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            scenario.acceptVisitor(textifier);
            return ResponseEntity.ok(textifier.getText());
        }
    }

    /**
     * Funkcja po przetworzeniu zwraca wskazany scenariusz pod nowa nazwa i zapisuje przetworzony scenariusz w
     * repozytorium. Pozwala na zmiane scernariusza tak, aby zawieral podscenariusze tylko do okreslonego poziomu
     * Uzywany url: ip_hosta/toLevel/ {id} {lvl} {name}
     *
     * @param id    ID scenariusza, ktory ma zostac przetworzony
     * @param level Maksyamalny poziom zaglebienia, ktory ma zawawierac przetworzony scenariusz. Musi byc >= 1
     * @param name  Nazwa, pod ktora ma zostac zapisany przetworzony scenariusz
     * @return Odpwoiedz HTTP, w ktorej ciele znajduje sie JSON z przetoworzonym scenariuszem, lub blad 404 jesli nie
     * znaleziono scenariusza o wskazanym ID
     */
    @GetMapping("/toLevel/{id} {lvl} {name}")
    public ResponseEntity<Scenario> getScenarioToLevel(@PathVariable("id") int id, @PathVariable("lvl") int level, @PathVariable("name") String name) {
        Scenario toConvert = scenarioRepository.getScenario(id);
        if (toConvert == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            SubLevelsVisitor visitor = null;
            try {
                visitor = new SubLevelsVisitor(level, name);
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
            toConvert.acceptVisitor(visitor);
            scenarioRepository.addScenario(visitor.getConverted());
            return ResponseEntity.ok(visitor.getConverted());
        }
    }

    /**
     * Pozwala sprawdzic, ile krokow scenariusza zaczyna sie od slow kluczowych
     * Uzywany url: ip_hosta/keywordsNumber/{id}
     *
     * @param id ID scenariusza do przetworzenia
     * @return Odpowiedz HTTP z JSON'em o liczbie krokow zaczynajacych sie od slow kluczowych lub blad 404 jesli nie
     * znaleziono scenariusza o wsakzanym ID
     */
    @GetMapping("/keywordsNumber/{id}")
    public ResponseEntity<Integer> getScenarioKeywordsNumber(@PathVariable("id") int id) {
        Scenario scenario = scenarioRepository.getScenario(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.HowManyDecisions());
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Funkcja pozwala sprawdzic, ktore kroki scenariusza nie zaczynaja sie od aktora lub aktora systemowego
     * Uzywany url: ip_hosta/noActorsStep/{id}
     *
     * @param id ID scenariusza do przetworzenia
     * @return Odpowiedz HTTP z JSON'em, ktory zawiera liste krokow, nie zaczynajacych sie od aktorow lub blad 404 jesli nie
     * znaleziono scenariusza o wsakzanym ID
     */
    @GetMapping("/noActorsStep/{id}")
    public ResponseEntity<List<String>> getScenarioStepsWithoutActors(@PathVariable("id") int id) {
        Scenario scenario = scenarioRepository.getScenario(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.ShowActorsErrors());
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Funkcja pozwala sprawdzic ile krokow zawiera caly scenariusz (lacznie z pod-scenariuszami)
     * Uzywany url: ip_hosta/length/{id}
     *
     * @param id ID scenariusza do sprawdzenia
     * @return Odpowiedz HTTP z JSON'em o liczbie krokow i podkrokow w scenariuszu
     */
    @GetMapping("/length/{id}")
    public ResponseEntity<Integer> getScenarioLength(@PathVariable("id") int id) {
        Scenario scenario = scenarioRepository.getScenario(id);
        if (scenario != null) {
            LengthVisitor visitor = new LengthVisitor();
            scenario.acceptVisitor(visitor);
            return ResponseEntity.ok(visitor.getSize());
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}