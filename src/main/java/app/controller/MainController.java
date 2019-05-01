package app.controller;

import app.model.ApplicationExecutionSettings;
import app.model.Operations;
import app.model.ProtectionCustomSetting;
import app.service.TaskManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private LinkedHashMap<String, Integer> protectionLevelsOptions;
    private final String protectionAddRandomError = "Add Random Error";
    private final String protectionCorrectErrors = "Correct Errors";

    @FXML
    private TextField sourcePathTextField;

    @FXML
    private TextField outputPathTextField;


    @FXML
    private ChoiceBox<String> protectionLevelsChoiceBox;

    @FXML
    private CheckBox protectionCustomSetting;


    @FXML
    private ChoiceBox<String> operationsChoiceBox;

    @FXML
    private VBox protectionAdvancedSettingsContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOperations();
        setProtectionLevelOptions();
        setProtectionDefaultCustomSetting();
    }

    @FXML
    private void runApplication() {
        System.out.println("Running app...");

        ApplicationExecutionSettings settings = new ApplicationExecutionSettings(
                sourcePathTextField.getText(),
                outputPathTextField.getText(),
                getOperations(),
                getProtectionLevel(),
                getProtectionCustomSetting()
        );
        try {
            String result = TaskManager.runApplicationWithSettings(settings);
            System.out.println(result);
        } catch (Error error) {
            System.out.println(error.getMessage());
        }
    }

    @FXML
    private void setSourcePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            sourcePathTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void setOutputPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Information Theory File (.ift)", ".ift"
                )
        );
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            outputPathTextField.setText(file.getAbsolutePath());
        }
    }

    private void setOperations() {
        operationsChoiceBox.setItems(
                FXCollections.observableArrayList(
                        "Protect",
                        "Unlock",
                        "Compress",
                        "Decompress",
                        "Protect & Compress",
                        "Unlock & Decompress"
                )
        );
        operationsChoiceBox.getSelectionModel().selectFirst();
        // Handles the Protection Settings display on Operation change
        operationsChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newValue) -> {
                    Operations op = Operations.fromString(newValue);
                    switch (op) {
                        case PROTECT:
                        case PROTECT_AND_COMPRESS:
                            protectionAdvancedSettingsContainer.setVisible(true);
                            protectionCustomSetting.setText(protectionAddRandomError);
                            break;
                        case UNLOCK:
                        case UNLOCK_AND_DECOMPRESS:
                            protectionAdvancedSettingsContainer.setVisible(true);
                            protectionCustomSetting.setText(protectionCorrectErrors);
                            break;
                        case COMPRESS:
                        case DECOMPRESS:
                            protectionAdvancedSettingsContainer.setVisible(false);
                            break;
                    }
                }
        );
    }

    private void setProtectionLevelOptions() {
        protectionLevelsOptions = new LinkedHashMap<String, Integer>() {
            {
                put("Extreme (7)", 7);
                put("High (32)", 32);
                put("Medium (1024)", 1024);
                put("Low (32768)", 32768);
            }
        };
        protectionLevelsChoiceBox.setItems(
                FXCollections.observableArrayList(protectionLevelsOptions.keySet())
        );
        protectionLevelsChoiceBox.getSelectionModel().selectFirst();
    }

    private void setProtectionDefaultCustomSetting() {
        protectionCustomSetting.setText(protectionAddRandomError);
    }

    private Operations getOperations() {
        String selectedValue = operationsChoiceBox.getValue();
        return Operations.fromString(selectedValue);
    }

    private int getProtectionLevel() {
        String selectedValue = protectionLevelsChoiceBox.getValue();
        return protectionLevelsOptions.get(selectedValue);
    }

    private ProtectionCustomSetting getProtectionCustomSetting() {
        if (protectionCustomSetting.isSelected()) {
            return ProtectionCustomSetting.fromString(
                    protectionCustomSetting.getText()
            );
        } else {
            return ProtectionCustomSetting.NOTHING;
        }
    }
}
