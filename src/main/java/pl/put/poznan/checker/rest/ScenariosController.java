package pl.put.poznan.checker.rest;

import pl.put.poznan.checker.scenario.ScenarioRepository;
import pl.put.poznan.checker.scenario.Scenario;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scenarios")
public class ScenariosController {
    private ScenarioRepository scenarioRepository = new ScenarioRepository();

    @PostMapping("/add")
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
}