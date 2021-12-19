package pl.put.poznan.checker.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.ScenarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.scenario.ScenarioTextifier;
import pl.put.poznan.checker.scenario.SubLevelsVisitor;

import java.util.HashMap;

@RestController
@RequestMapping("/scenarios")
public class ScenariosController {
    private static final Logger logger = LoggerFactory.getLogger(ScenariosController.class);
    ScenarioRepository scenarioRepository = new ScenarioRepository();

    @PostMapping("/")
    public void addScenario(@RequestBody Scenario scenario) {
        logger.info("addScenario(Scenario scenario) próbuje dodać scenariusz: " + scenario.getName() );
        scenarioRepository.addScenario(scenario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getScenario(@PathVariable("id") Integer id) {
        var scenario = scenarioRepository.getScenario(id);

        if (scenario == null)
        {
            logger.warn("getScenario(Integer id) próbował zwrócić nieistniejący scenariusz o numerze " + id.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            logger.info("getScenario(Integer id) zwraca scenariusz o numerze " + id.toString() + " i nazwie \""
                    + scenario.getName() + "\"" );
            return ResponseEntity.ok(scenario);
        }
    }

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

    @GetMapping("/byName/{name}")
    public ResponseEntity<Scenario> getScenarioByName(@PathVariable("name") String name) {
        Scenario act = scenarioRepository.getScenarioByName(name);
        if (act == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(act);
    }

    @GetMapping("/text/{id}")
    public ResponseEntity<String> getTextifiedScenario(@PathVariable("id") Integer id){
        var scenario = scenarioRepository.getScenario(id);
        var textifier = new ScenarioTextifier();
        if (scenario == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            scenario.acceptVisitor(textifier);
            System.out.println("Scenario textified " + id + ":");
            System.out.println(textifier.getText());
            return ResponseEntity.ok(textifier.getText());
        }
    }
    @GetMapping("/toLevel/{id} {lvl} {name}")
    public ResponseEntity<Scenario>getScenarioToLevel(@PathVariable("id") int id, @PathVariable("lvl") int level, @PathVariable("name")String name){
        Scenario toConvert = scenarioRepository.getScenario(id);
        if(toConvert == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else{
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
}