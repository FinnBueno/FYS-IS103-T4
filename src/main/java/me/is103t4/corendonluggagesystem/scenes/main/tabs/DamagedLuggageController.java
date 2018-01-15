package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTypesTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.RegisterLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

/**
 * DamagedLuggage
 *
 * @author imranmohmand
 */
public class DamagedLuggageController extends Controller {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField luggageIDField;

    @FXML
    private ComboBox<String> flightNumberBox;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField repairCosts;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private File photo;

    @FXML
    private Button registerButton;

    @Override
    public boolean isOpen() {
        return Tabs.DAMAGED_LUGGAGE.isOpen(0);
    }

    @Override
    public void postInit(ResourceBundle bundle) {
        // set enter button
        setEnterButton(registerButton);

        FetchLuggageTypesTask task = new FetchLuggageTypesTask();
        task.setOnSucceeded(event -> typeBox.setItems(FXCollections.observableList((List<String>) task.getValue())));
        FetchAirlinesTask task1 = new FetchAirlinesTask();
        task1.setOnSucceeded(event -> flightNumberBox.setItems(FXCollections.observableList((List<String>) task1.getValue())));

        luggageIDField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (!newValue.matches("\\d*"))
                        luggageIDField.setText(newValue.
                                replaceAll("[^\\d]", ""));
                });

        phoneNumberField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.length() > 11)
                        newValue = newValue.substring(0, 11);
                    if (!newValue.matches("\\d*"))
                        newValue = newValue.replaceAll("[^\\d]", "");
                    phoneNumberField.setText(newValue);
                });
        repairCosts.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.length() > 11)
                        newValue = newValue.substring(0, 11);
                    if (!newValue.matches("\\d*"))
                        newValue = newValue.replaceAll("[^\\d]", "");
                    repairCosts.setText(newValue);
                });
    }

    @FXML
    private void registerDamagedLuggage() {
        if (checkEmptyFields()) {
            AlertBuilder.NOT_ALL_REQUIRED_FILLED.showAndWait();
            return;
        }

        RegisterLuggageTask task = new RegisterLuggageTask("Damaged", firstNameField.getText(), lastNameField.getText(),
                "", "", "", phoneNumberField.getText(), emailField.getText(), "",
                typeBox.getSelectionModel().getSelectedItem(), luggageIDField.getText(), "", null,
                descriptionField.getText(), photo, flightNumberBox.getSelectionModel().getSelectedItem(), Account.getLoggedInUser(), Integer
                .parseInt(repairCosts.getText() == null || repairCosts.getText().length() == 0 ? "0" : repairCosts.getText()));
        task.setOnSucceeded(event -> {
            boolean succeeded = (boolean) task.getValue();
            if (succeeded) AlertBuilder.CHANGES_SAVED.showAndWait();
            else AlertBuilder.ERROR_OCCURRED.showAndWait();
        });
    }

    private boolean checkEmptyFields() {
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0)
            return true;
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0)
            return true;
        if (phoneNumberField.getText() == null || phoneNumberField.getText().length() == 0)
            return true;
        if (luggageIDField.getText() == null || luggageIDField.getText().length() == 0)
            return true;
        if (flightNumberBox.getSelectionModel().getSelectedItem() == null)
            return true;
        if (emailField.getText() == null || emailField.getText().length() == 0)
            return true;
        if (repairCosts.getText() == null || repairCosts.getText().length() == 0)
            return true;
        if (descriptionField.getText() == null || descriptionField.getText().length() == 0)
            return true;
        if (typeBox.getSelectionModel().getSelectedItem() == null)
            return true;
        return false;
    }

    @FXML
    private void selectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().
                addAll(
                        new FileChooser.ExtensionFilter("All Files", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
        File file = fileChooser.showOpenDialog(main.getStage());

        if (file != null && file.exists())
            photo = file;
    }

}
