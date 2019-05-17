package app.controller;

import app.model.ApplicationExecutionSettings;
import app.model.Operations;
import app.model.ProtectionCustomSetting;
import app.model.TimeLockSettings;
import app.service.TaskManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

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
    private TitledPane protectionAdvancedSettingsContainer;

    @FXML
    private TitledPane timeLockContainer;

    @FXML
    private CheckBox timeLockCheckBox;

    @FXML
    private DatePicker timeLockUnlockDate;

    @FXML
    private Button runButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOperations();
        setProtectionLevelOptions();
        setProtectionDefaultCustomSetting();
    }

    @FXML
    private void runApplication() {
        ApplicationExecutionSettings settings = getApplicationExecutionSettings();
        runApplicationAsync(settings);
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
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            outputPathTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void openSite() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("http://www.unsl.edu.ar/"));
            } catch (Exception ignored) {
            }
        }
    }

    @FXML
    private void displayAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Information Theory App");
        alert.setContentText(
                "Cross-platform Desktop App for data Protection and Compression using Hamming's and Huffman's algorithms\n\n" +
                        "Built using Java 8 and JavaFX\n\n" +
                        "2019 - UNSL - Argentina"
        );
        alert.showAndWait();
    }

    private void runApplicationAsync(ApplicationExecutionSettings settings) {
        // Change the UI to reflect that the app is running
        this.runButton.setDisable(true);
        this.runButton.getScene().setCursor(Cursor.WAIT);
        // Run tasks asynchronously
        CompletableFuture
                .supplyAsync(() -> TaskManager.runApplicationWithSettings(settings))
                .handle((elapsedTime, error) -> {
                    if (error == null) {
                        Platform.runLater(() -> displaySucces(elapsedTime));
                    } else {
                        Platform.runLater(() -> displayError(error.getCause().getMessage()));
                    }
                    // Restore the UI
                    Platform.runLater(() -> {
                        this.runButton.setDisable(false);
                        this.runButton.getScene().setCursor(Cursor.DEFAULT);
                    });
                    return null;
                });
    }

    private ApplicationExecutionSettings getApplicationExecutionSettings() {
        return new ApplicationExecutionSettings(
                sourcePathTextField.getText(),
                outputPathTextField.getText(),
                getOperations(),
                getProtectionLevel(),
                getProtectionCustomSetting(),
                getTimeLockSettings()
        );
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
                            timeLockContainer.setDisable(false);
                            protectionAdvancedSettingsContainer.setDisable(false);
                            protectionCustomSetting.setText(protectionAddRandomError);
                            break;
                        case UNLOCK:
                        case UNLOCK_AND_DECOMPRESS:
                            timeLockContainer.setDisable(true);
                            protectionAdvancedSettingsContainer.setDisable(false);
                            protectionCustomSetting.setText(protectionCorrectErrors);
                            break;
                        case COMPRESS:
                            timeLockContainer.setDisable(false);
                            protectionAdvancedSettingsContainer.setDisable(true);
                            break;
                        case DECOMPRESS:
                            timeLockContainer.setDisable(true);
                            protectionAdvancedSettingsContainer.setDisable(true);
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

    private TimeLockSettings getTimeLockSettings() {
        return new TimeLockSettings(
                timeLockCheckBox.isSelected(),
                timeLockUnlockDate.getValue()
        );
    }

    private void displaySucces(long elapsedTime) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succes");
        alert.setHeaderText(null);
        alert.setContentText(
                "Operation(s) finished successfully in " + elapsedTime + " ms"
        );
        alert.showAndWait();
    }

    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ooops, something went wrong!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
