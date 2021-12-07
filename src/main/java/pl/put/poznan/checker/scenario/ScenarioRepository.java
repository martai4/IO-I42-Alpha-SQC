package pl.put.poznan.checker.scenario;

import pl.put.poznan.checker.scenario.Scenario;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScenarioRepository {
    private Map<Integer, Scenario> scenarios = new ConcurrentHashMap<>();
    private AtomicInteger lastGeneratedId = new AtomicInteger(0);

    public Integer saveScenario(Scenario scenario) {
        Integer id = lastGeneratedId.incrementAndGet();

        scenarios.put(id, scenario);
        return id;
    }

    public Scenario getScenario(Integer id) {
        return scenarios.get(id);
    }


}
