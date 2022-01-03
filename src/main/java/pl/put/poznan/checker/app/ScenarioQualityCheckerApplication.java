package pl.put.poznan.checker.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Serce programu w wersji <b>zdalnego API</b>.
 *
 * @author I42-Alpha
 * @version 2.0
 */
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.checker.rest"})
public class ScenarioQualityCheckerApplication
{
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerApplication.class);

    /**
     * Domyślny konstruktor <code>ScenarioQualityCheckerApplication</code>.
     */
    public ScenarioQualityCheckerApplication() {}

    /**
     * Główna metoda uruchamiana przy starcie programu.
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args)
    {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        logger.info("Program wystartował.");
        SpringApplication.run(ScenarioQualityCheckerApplication.class, args);
    }
}
