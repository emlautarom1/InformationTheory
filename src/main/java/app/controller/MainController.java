package app.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final String protectionAddRandomError = "Add Random Error";
    private final String protectionCorrectErrors = "Correct Errors";

    @FXML
    private TextField sourcePathTextField;

    @FXML
    private TextField outputPathTextField;

    @FXML
    private ChoiceBox<String> protectionSettingsChoiceBox;

    @FXML
    private ChoiceBox<String> protectionSettingsLevelsChoiceBox;

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
        protectionSettingsLevelsChoiceBox.setDisable(!value);
        protectionCustomSetting.setDisable(!value);
    }

    private void setProtectionLevelOptions() {
        LinkedHashMap<String, Integer> protectionSettingLevelsOptions = new LinkedHashMap<String, Integer>() {
            {
                put("Extreme (7)", 7);
                put("High (32)", 32);
                put("Medium (1024)", 1024);
                put("Low (32768)", 32768);
            }
        };
        protectionSettingsLevelsChoiceBox.setItems(
                FXCollections.observableArrayList(protectionSettingLevelsOptions.keySet())
        );
        protectionSettingsLevelsChoiceBox.getSelectionModel().selectFirst();
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

}
