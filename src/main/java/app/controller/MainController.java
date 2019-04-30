package app.controller;

import app.model.ApplicationExecutionSettings;
import app.model.CompressionSettings;
import app.model.ProtectionCustomSetting;
import app.model.ProtectionSettings;
import app.service.TaskManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    private ChoiceBox<String> protectionSettingsChoiceBox;

    @FXML
    private ChoiceBox<String> protectionLevelsChoiceBox;

    @FXML
    private CheckBox protectionCustomSetting;

    @FXML
    private ChoiceBox<String> compressionSettingsChoiceBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setProtectionSettingsOptions();
        setProtectionLevelOptions();
        setProtectionDefaultCustomSetting();
        setCompressionSettingsOptions();
    }

    @FXML
    private void runApplication() {
        System.out.println("Running app...");

        ApplicationExecutionSettings settings = new ApplicationExecutionSettings(
                sourcePathTextField.getText(),
                outputPathTextField.getText(),
                ProtectionSettings.fromString(
                        protectionSettingsChoiceBox.getValue()
                ),
                getProtectionLevel(),
                getProtectionCustomSetting(),
                CompressionSettings.fromString(
                        compressionSettingsChoiceBox.getValue()
                )
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

    private void setProtectionSettingsOptions() {
        List<String> protectionSettingsOptions = new ArrayList<String>() {
            {
                add("Nothing");
                add("Protect");
                add("Unlock");
            }
        };
        protectionSettingsChoiceBox.setItems(
                FXCollections.observableArrayList(protectionSettingsOptions)
        );
        protectionSettingsChoiceBox.getSelectionModel().selectFirst();
        // Disable custom parameters
        enableProtectionSettingsVisibility(false);
        protectionSettingsChoiceBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, newValue) -> {
                    switch (newValue) {
                        case "Nothing":
                            enableProtectionSettingsVisibility(false);
                            break;
                        case "Protect":
                            enableProtectionSettingsVisibility(true);
                            protectionCustomSetting.setText(protectionAddRandomError);
                            protectionCustomSetting.setSelected(false);
                            break;
                        case "Unlock":
                            enableProtectionSettingsVisibility(true);
                            protectionCustomSetting.setText(protectionCorrectErrors);
                            protectionCustomSetting.setSelected(false);
                            break;
                    }
                });
    }

    private void enableProtectionSettingsVisibility(boolean value) {
        protectionLevelsChoiceBox.setDisable(!value);
        protectionCustomSetting.setDisable(!value);
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

    private void setCompressionSettingsOptions() {
        List<String> compressionSettingsOptions = new ArrayList<String>() {
            {
                add("Nothing");
                add("Compress");
                add("Decompress");
            }
        };
        compressionSettingsChoiceBox.setItems(
                FXCollections.observableArrayList(compressionSettingsOptions)
        );
        compressionSettingsChoiceBox.getSelectionModel().selectFirst();
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
