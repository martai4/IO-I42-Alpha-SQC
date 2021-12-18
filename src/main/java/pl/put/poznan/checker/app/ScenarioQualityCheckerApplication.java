package pl.put.poznan.checker.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerApplication.class);
    /**
     * Główna metoda uruchamiana przy starcie programu
     * @param args argumenty wiersza poleceń
     * */
    public static void main(String[] args) {
        logger.info("wystartowano");
        SpringApplication.run(ScenarioQualityCheckerApplication.class, args);
    }
}
