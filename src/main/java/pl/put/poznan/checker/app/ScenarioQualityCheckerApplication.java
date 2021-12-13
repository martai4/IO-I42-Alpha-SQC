package pl.put.poznan.checker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Serce programu w wersji zdalnego API
 *
 * @author I42-Alpha
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.checker.rest"})
public class ScenarioQualityCheckerApplication {
    /**
     * Główna metoda uruchamiana przy starcie programu
     * @param args argumenty wiersza poleceń
     * */
    public static void main(String[] args) {
        SpringApplication.run(ScenarioQualityCheckerApplication.class, args);
    }
}
