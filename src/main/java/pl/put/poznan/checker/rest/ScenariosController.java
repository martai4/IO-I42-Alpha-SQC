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

//todo: dodać javadocs i logging oraz testy mockiem
@RestController
@RequestMapping("/scenarios")
public class ScenariosController
{
    private static final Logger logger = LoggerFactory.getLogger(ScenariosController.class);
    private final ScenarioRepository scenarioRepository = new ScenarioRepository();

    /**
     * Domyślny konstruktor <code>ScenariosController</code>.
     */
    ScenariosController(){}

    @PostMapping("/")
    public void addScenario(@RequestBody Scenario scenario) {
        logger.info("Zdalne API próbuje dodać Scenariusz \"{}\"", scenario.getName());
        scenarioRepository.addScenario(scenario);
    }

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

    @GetMapping("/toLevel/{id} {lvl} {name}")
    public ResponseEntity<Scenario>getScenarioToLevel(@PathVariable("id") Integer id, @PathVariable("lvl") int level, @PathVariable("name")String name)
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