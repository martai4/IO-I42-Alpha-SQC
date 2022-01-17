package pl.put.poznan.checker.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.put.poznan.checker.debug.ScenarioFileLoader;
import pl.put.poznan.checker.logic.ScenarioFormat;
import pl.put.poznan.checker.logic.ScenarioQualityChecker;
import pl.put.poznan.checker.scenario.Scenario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScenarioQualityCheckerGUIApplicationController
{
    private String Path = "";

    private Stage stage;
    @FXML
    private Label size, decisions, errors;
    @FXML
    private Button check;
    @FXML
    private TextArea textEditor;
    @FXML
    private MenuItem open, save, saveAs, exit;
    @FXML
    private ListView<String> lista;

    private void reset()
    {
        size.setText("Size: ");
        decisions.setText("Decisions: ");
        lista.getItems().clear();
        size.setDisable(true);
        decisions.setDisable(true);
        errors.setDisable(true);
        lista.setDisable(true);
    }

    private void readFailCheck(Scenario scenario)
    {
        if (scenario != null)
            return;
        if (!textEditor.getText().contains(ScenarioFormat.titleFormat))
        {
            lista.getItems().add("Failed to load file: no title!");
            lista.getItems().add("Didn't find \"" + ScenarioFormat.titleFormat + "\"");
        }
        else if (!textEditor.getText().contains(ScenarioFormat.actorFormat))
        {
            lista.getItems().add("Failed to load file: no actors!");
            lista.getItems().add("Didn't find \"" + ScenarioFormat.actorFormat + "\"");
        }
        else if (!textEditor.getText().contains(ScenarioFormat.systemActorFormat))
        {
            lista.getItems().add("Failed to load file: no system actor!");
            lista.getItems().add("Didn't find \"" + ScenarioFormat.systemActorFormat + "\"");
        }
        else
        {
            lista.getItems().add("Failed to load file!");
            lista.getItems().add("Probably inconsistent numeration/delimeters");
        }
    }

    private void saveScenario(Scenario scenario)
    {
        if (scenario == null)
            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(Path, StandardCharsets.UTF_8));
                writer.write(textEditor.getText());
                writer.close();
            } catch (IOException ex)
            {
                //todo: log
            }
        else //w ten sposób poprawimy numerację, jeżeli użytkownik ją źle wpisał
            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(Path, StandardCharsets.UTF_8));
                String scenarioText = ScenarioQualityChecker.getScenarioTextified(scenario);
                writer.write(ScenarioQualityChecker.getScenarioTextified(scenario));
                writer.close();
                textEditor.setText(scenarioText);
            } catch (IOException ex)
            {
                //todo: log
            }
    }

    @FXML
    public void openClick(ActionEvent event)
    {
        Path = "";
        reset();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Scenario File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        File f = fileChooser.showOpenDialog(stage);
        Scenario scenario = null;
        if (f == null)
            return;
        Path = f.getPath();
        textEditor.clear();
        try {
            textEditor.setText(Files.readString(Paths.get(Path), StandardCharsets.UTF_8));
            scenario = ScenarioFileLoader.loadScenario(new BufferedReader(new StringReader(textEditor.getText())));
        }
        catch (IOException ex)
        {
            //todo: log
        }
        finally
        {
            readFailCheck(scenario);
        }
    }

    @FXML
    public void saveClick(ActionEvent event)
    {
        try {
            Scenario scenario = ScenarioFileLoader.loadScenario(new BufferedReader(new StringReader(textEditor.getText())));
            saveScenario(scenario);
        }
        catch (IOException ex)
        {
            //todo:log
        }
    }

    @FXML
    public void saveAsClick(ActionEvent event)
    {
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
            File f = fileChooser.showSaveDialog(stage);
            if (f == null)
                return;
            Path = f.getPath();
            Scenario scenario = ScenarioFileLoader.loadScenario(new BufferedReader(new StringReader(textEditor.getText())));

            saveScenario(scenario);
        }
        catch (IOException ex)
        {
            Path = "";
            reset();
            //todo: log
        }
    }

    @FXML
    public void exitClick(ActionEvent event)
    {
        event.consume();
        System.exit(0);
    }

    @FXML
    public void checkClick(ActionEvent event)
    {
        reset();
        Scenario scenario = null;

        try {
            scenario = ScenarioFileLoader.loadScenario(new BufferedReader(new StringReader(textEditor.getText())));
        }
        catch (IOException ex)
        {
            scenario = null;
            //todo:log
        }
        finally
        {
            readFailCheck(scenario);
        }
        if (scenario == null)
            return;
        size.setDisable(false);
        decisions.setDisable(false);
        errors.setDisable(false);
        lista.setDisable(false);
        size.setText("Size: " + Integer.toString(ScenarioQualityChecker.getScenarioSize(scenario)));
        decisions.setText("Decisions: " + Integer.toString(ScenarioQualityChecker.getDecisionCount(scenario)));
        for (String error : ScenarioQualityChecker.getActorErrors(scenario))
            lista.getItems().add(error);
    }

    public void setStageAndSetupListeners(Stage stage)
    {
        this.stage = stage;
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("sem5.png")));
    }
}
