/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.io.File;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTypesTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.RegisterLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
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
    public TextField luggageIDField;

    @FXML
    public ComboBox<String> flightNumberBox;

    @FXML
    public ComboBox<String> typeBox;

    @FXML
    public TextField brandField;

    @FXML
    public ColorPicker colorPicker;

    @FXML
    public TextField characsField;

    @FXML
    private File photo;

    @FXML
    public Button registerButton;

    @Override
    public boolean isOpen() {
        return Tabs.FOUND_LUGGAGE.isOpen(0);
    }

    @Override
    public void postInit() {

        // set enter button
        setEnterButton(registerButton);

        // I honestly have no clue what the hell is going on here, this check is the only way I found to possible fix it
        if (typeBox == null)
            return;

        FetchAirlinesTask airlinesTask = new FetchAirlinesTask();
        airlinesTask.setOnSucceeded(v -> flightNumberBox.setItems(FXCollections.observableArrayList((List<String>)
                airlinesTask.getValue())));

        FetchLuggageTypesTask task = new FetchLuggageTypesTask();
        task.setOnSucceeded(v -> typeBox.setItems(FXCollections.observableArrayList((List<String>) task.getValue())));
    }

    @FXML
    private void registerFoundLuggage() {
        if (checkEmptyFields()) {
            return;
        }

        System.out.println("Found register called");

        RegisterLuggageTask registerLuggageTask = new RegisterLuggageTask("Found", null, null, null, null,
                null, null, null, null, typeBox.getSelectionModel().getSelectedItem(), luggageIDField.getText(),
                brandField.getText(), colorPicker.getValue(), characsField
                .getText(), photo, flightNumberBox.getSelectionModel().getSelectedItem(), Account.getLoggedInUser());

        System.out.println("Found register called 2");

        registerButton.setDisable(true);
        registerLuggageTask.setOnFailed(v -> {
            System.out.println(registerLuggageTask.getException());
            registerButton.setDisable(false);
            AlertBuilder.ERROR_OCCURRED.showAndWait();
        });
        registerLuggageTask.setOnSucceeded(v -> {
            registerButton.setDisable(false);

            AlertBuilder.REGISTERED_LUGGAGE.showAndWait().orElse(null);
            Platform.runLater(() -> Tabs.OVERVIEW.setRoot(0));
        });

    }

    private boolean checkEmptyFields() {
        if (characsField.getText() == null || characsField.getText().length() == 0) {
            alert("Characteristics cannot be empty!");
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
