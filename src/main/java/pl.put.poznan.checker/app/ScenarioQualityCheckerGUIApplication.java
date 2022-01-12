package pl.put.poznan.checker.app;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

/**
 * Serce programu w wersji graficznego interfejsu.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public class ScenarioQualityCheckerGUIApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerGUIApplication.class);

    /**
     * Domyślny konstruktor <code>ScenarioQualityCheckerGUIApplication</code>.
     * */
    public ScenarioQualityCheckerGUIApplication() {}
    @Override        /*
         tryScenarioFormat.
        {
            scenarioFile = new FileReader(file);
            //Pozwala na odczyt linia po linii
            buffer = new BufferedReader(scenarioFile);
        }
        catch (IOException e)
        {
            logger.error("loadScenario(String): blad odczytu z pliku");
            throw e;
        }
         */
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("app/ScenarioQualityCheckerGUIApplication.fxml"));
        Scene scene = new Scene(root);
        logger.info("Program wystartował");
        stage.setTitle("My First JavaFX App");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Główna metoda uruchamiana przy starcie programu.
     * @param args argumenty wiersza poleceń
     * */
    public static void main(String[] args) {
        Application.launch(args);
    }

}
