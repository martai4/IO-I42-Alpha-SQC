package pl.put.poznan.checker.scenario;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScenarioRepository
{
    private final Map<Integer, Scenario> scenarios = new ConcurrentHashMap<>();
    private final AtomicInteger lastGeneratedId = new AtomicInteger(0);

    public Integer saveScenario(Scenario scenario)
    {
        Integer id = lastGeneratedId.incrementAndGet();

        scenarios.put(id, scenario);
        return id;
    }

    public Scenario getScenario(Integer id) {
        return scenarios.get(id);
    }

    public Map<Integer, Scenario> getScenarios() {
        return scenarios;
    }
}
