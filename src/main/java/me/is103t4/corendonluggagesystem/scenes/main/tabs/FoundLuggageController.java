/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.LuggageType;
import me.is103t4.corendonluggagesystem.database.RegisterType;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.RegisterLuggageTask;
import me.is103t4.corendonluggagesystem.email.EmailSender;
import me.is103t4.corendonluggagesystem.email.IEmail;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

/**
 * FXML Controller class
 *
 * @author timvanekert
 */
public class FoundLuggageController extends Controller {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField luggageIDField;

    @FXML
    private ComboBox<String> flightNumberBox;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TextField brandField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField characsField;

    @FXML
    private File photo;

    @FXML
    private Button registerButton;

    @Override
    public boolean isOpen() {
        return Tabs.FOUND_LUGGAGE.isOpen(0);
    }

    @Override
    public void postInit() {
        // set enter button
        setEnterButton(registerButton);
    }

    @FXML
    private void registerFoundLuggage() {

        if (checkEmptyFields())
            return;

        RegisterLuggageTask registerLuggageTask = new RegisterLuggageTask(RegisterType.LOST, firstNameField.getText()
                , lastNameField.getText(), null, null, null, null, null, null, LuggageType.viaLocale(typeBox
                .getSelectionModel().getSelectedItem(), Locale.ENGLISH), luggageIDField.getText(), brandField.getText
                (), colorPicker.getValue(), characsField.getText(), photo, flightNumberBox.getSelectionModel().getSelectedItem(), Account
                .getLoggedInUser());

        registerButton.setDisable(true);
        registerLuggageTask.setOnFailed(v -> {
            registerButton.setDisable(false);
            AlertBuilder.ERROR_OCCURED.showAndWait();
        });
        registerLuggageTask.setOnSucceeded(v -> {
            registerButton.setDisable(false);

            Optional<ButtonType> optional = AlertBuilder.REGISTERED_LUGGAGE.showAndWait();
            if (!optional.isPresent())
                return;

            ButtonType type = optional.get();
            if (type.getText().equals("Search for matches")) {
                // TODO: Find match
                AlertBuilder.ERROR_OCCURED.showAndWait();
            }
        });

    }

    private boolean checkEmptyFields() {
        if (characsField.getText() == null || characsField.getText().length() == 0) {
            alert("Characteristics cannot be empty!");
            return true;
        }
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            alert("First name cannot be empty!");
            return true;
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            alert("Last name cannot be empty!");
            return true;
        }
        if (typeBox.getSelectionModel().getSelectedItem() == null) {
            alert("Luggage type must be selected!");
            return true;
        }
        return false;
    }

    @FXML
    private void selectPhoto() {
        File file = openFileSelectPrompt(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        if (file != null && file.exists())
            photo = file;
    }
}
