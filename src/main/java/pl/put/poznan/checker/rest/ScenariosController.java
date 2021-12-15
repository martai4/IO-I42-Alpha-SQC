package pl.put.poznan.checker.rest;

import pl.put.poznan.checker.scenario.ScenarioRepository;
import pl.put.poznan.checker.scenario.Scenario;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.scenario.ScenarioTextifier;

import java.util.HashMap;

@RestController
@RequestMapping("/scenarios")
public class ScenariosController {
    private ScenarioRepository scenarioRepository = new ScenarioRepository();

    @PostMapping("/")
    public Integer addScenario(@RequestBody Scenario scenario) {
        return scenarioRepository.saveScenario(scenario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getAllScenarios(@PathVariable("id") Integer id) {
        var scenario = scenarioRepository.getScenario(id);

        if (scenario == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(scenario);
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<HashMap<Integer, String>> listAllScenarios() {
        HashMap<Integer, String> scenarios = new HashMap<>();
        for (var key : scenarioRepository.getScenarios().keySet()) {
            scenarios.put(key, scenarioRepository.getScenario(key).getName());
        }
        return ResponseEntity.ok(scenarios);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<Scenario> getScenarioByName(@PathVariable("name") String name) {
        for (var key : scenarioRepository.getScenarios().keySet()) {
            Scenario act = scenarioRepository.getScenarios().get(key);
            if (act.getName().equals(name)) {
                return ResponseEntity.ok(act);
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
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
}