/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTypesTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.RegisterLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.CreateRegistrationCopyTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchCountriesTask;
import me.is103t4.corendonluggagesystem.email.EmailSender;
import me.is103t4.corendonluggagesystem.email.IEmail;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.matching.Matcher;
import me.is103t4.corendonluggagesystem.pdf.PDF;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Finn Bon
 */
public class LostLuggageController extends Controller {

    private static final long HOUR_PERIOD_6 = 6 * 60 * 60 * 1000;
    private static final long HOUR_PERIOD_24 = 24 * 60 * 60 * 1000;
    private static final long HOUR_PERIOD_72 = 72 * 60 * 60 * 1000;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField zipField;

    @FXML
    private ComboBox<String> countryBox;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

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
    private CheckBox colorUnknownCheckbox;

    @FXML
    private TextField characsField;

    @FXML
    private Button photoButton;

    @FXML
    private ComboBox<String> langBox;

    @FXML
    private Button registerButton;

    @FXML
    private File photo;

    @Override
    public boolean isOpen() {
        return Tabs.LOST_LUGGAGE.isOpen(0);
    }

    @Override
    public void postInit(ResourceBundle bundle) {
        // set enter action
        setEnterButton(registerButton);

        // turn some textfields into numberfields
        phoneNumberField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.length() > 10)
                        newValue = newValue.substring(0, 10);
                    if (!newValue.matches("\\d*"))
                        newValue = newValue.replaceAll("[^\\d]", "");
                    phoneNumberField.setText(newValue);
                });
        luggageIDField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.length() > 10)
                        newValue = newValue.substring(0, 10);
                    if (!newValue.matches("\\d*"))
                        newValue = newValue.replaceAll("[^\\d]", "");
                    phoneNumberField.setText(newValue);
                });

        // fill combo boxes
        FetchCountriesTask countryTask = new FetchCountriesTask();
        countryTask.setOnSucceeded(v -> countryBox.setItems(FXCollections.observableArrayList((List<String>)
                countryTask.getValue())));

        FetchAirlinesTask airlinesTask = new FetchAirlinesTask();
        airlinesTask.setOnSucceeded(v -> flightNumberBox.setItems(FXCollections.observableArrayList((List<String>)
                airlinesTask.getValue())));

        FetchLuggageTypesTask task = new FetchLuggageTypesTask();
        task.setOnSucceeded(v -> typeBox.setItems(FXCollections.observableArrayList((List<String>) task.getValue())));

        langBox.getItems().addAll(bundle.getString("english"), bundle.getString("dutch"));
    }

    @FXML
    private void registerLostLuggage() {

        if (checkEmptyFields())
            return;

        RegisterLuggageTask registerLuggageTask = new RegisterLuggageTask("Lost", firstNameField.getText()
                , lastNameField.getText(), addressField.getText(), cityField.getText(), zipField.getText(),
                phoneNumberField.getText(), emailField.getText(), langBox.getSelectionModel().getSelectedItem(),
                typeBox.getSelectionModel().getSelectedItem(), luggageIDField.getText(), brandField.getText(),
                colorUnknownCheckbox.isSelected() ? null : colorPicker.getValue(), characsField.getText(), photo,
                flightNumberBox.getSelectionModel().getSelectedItem(), Account.getLoggedInUser());

        registerButton.setDisable(true);
        registerLuggageTask.setOnSucceeded((Event v) -> {
            boolean inserted = (boolean) registerLuggageTask.getValue();
            if (!inserted) {
                AlertBuilder.ERROR_OCCURRED.showAndWait();
                registerButton.setDisable(false);
                return;
            }

            registerButton.setDisable(false);
            IEmail email = new IEmail("Lost Luggage Confirmation", true, emailField.
                    getText());
            email.setContentFromURL(getClass().
                    getResource("/email/lostRegisteredEmail.html"), true).
                    setParameters(txt -> {
                        txt = txt.replace("%%first%%", firstNameField.getText());
                        txt = txt.replace("%%last%%", lastNameField.getText());
                        txt = txt.replace("%%address%%", addressField.getText());
                        txt = txt.replace("%%city%%", cityField.getText());
                        txt = txt.replace("%%zip%%", zipField.getText());
                        txt = txt.replace("%%country%%", countryBox.
                                getSelectionModel().
                                getSelectedItem());
                        txt = txt.replace("%%phone%%", phoneNumberField.getText());
                        txt = txt.replace("%%email%%", emailField.getText());
                        txt = txt.replace("%%lugid%%", luggageIDField.getText());
                        txt = txt.replace("%%flight%%", flightNumberBox.getSelectionModel().getSelectedItem());
                        txt = txt.
                                replace("%%type%%", typeBox.
                                        getSelectionModel().
                                        getSelectedItem());
                        txt = txt.replace("%%brand%%", brandField.getText());
                        txt = txt.
                                replace("%%colour%%", colorUnknownCheckbox.
                                        isSelected() ? "Unknown" : "<span style=\"color: " + toHex(colorPicker.
                                        getValue()) + "; background-color: " + toHex(colorPicker.
                                        getValue()) + "\">|______________|</span>");
                        txt = txt.replace("%%characs%%", characsField.getText());
                        return txt;
                    });
            email.setAttachments(photo);

            EmailSender.getInstance().
                    send(email);

            // insurance claim
            File file = promptForInsuranceClaim();

            // registration copy
            promptForPDFCreation(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    cityField.getText(),
                    zipField.getText(),
                    countryBox.getSelectionModel().getSelectedItem(),
                    phoneNumberField.getText(),
                    emailField.getText(),
                    luggageIDField.getText(),
                    flightNumberBox.getSelectionModel().getSelectedItem(),
                    typeBox.getSelectionModel().getSelectedItem(),
                    brandField.getText(),
                    colorUnknownCheckbox.isSelected() ? "Unknown" : toHex(colorPicker.getValue()),
                    characsField.getText(),
                    langBox.getSelectionModel().getSelectedIndex() == 0 ? "English" : "Dutch", file);

            AlertBuilder.REGISTERED_LUGGAGE.showAndWait();
            Tabs.OVERVIEW.setRoot(0);
        });
    }

    private File promptForInsuranceClaim() {
        return new PDF("Insurance Claim", main.getStage()).exportInsurancePDF();
    }

    private boolean checkEmptyFields() {
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            alert(bundle.getString("firstEmpty"));
            return true;
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            alert(bundle.getString("lastEmpty"));
            return true;
        }
        if (addressField.getText() == null || addressField.getText().length() == 0) {
            alert(bundle.getString("addressEmpty"));
            return true;
        }
        if (cityField.getText() == null || cityField.getText().length() == 0) {
            alert(bundle.getString("cityEmpty"));
            return true;
        }
        if (zipField.getText() == null || zipField.getText().length() == 0) {
            alert(bundle.getString("zipEmpty"));
            return true;
        }
        if (phoneNumberField.getText() == null || zipField.getText().length() == 0 || !phoneNumberField.getText()
                .matches("\\d*")) {
            alert(bundle.getString("phoneEmpty"));
            return true;
        }
        if (emailField.getText() == null || emailField.getText().length() == 0) {
            alert(bundle.getString("emailEmpty"));
            return true;
        }
        if (langBox.getSelectionModel().getSelectedItem() == null) {
            alert(bundle.getString("langEmpty"));
            return true;
        }
        if (typeBox.getSelectionModel().getSelectedItem() == null) {
            alert(bundle.getString("luggageTypeEmpty"));
            return true;
        }
        if (flightNumberBox.getSelectionModel().getSelectedItem() == null) {
            alert(bundle.getString("flightEmpty"));
            return true;
        }
        return false;
    }

    private void promptForPDFCreation(String firstName, String lastName, String address, String city, String zip,
                                      String country, String phoneNumber, String email, String luggageId, String
                                              flight, String type, String brand, String colour, String
                                              characteristics, String language, File... files) {
        new CreateRegistrationCopyTask(firstName, lastName, address, city, zip, country, phoneNumber, email,
                luggageId, flight, type, brand, colour, characteristics, language, main.getStage(), files);
    }

    private String toHex(Color color) {
        if (color == null)
            return null;
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @FXML
    private void selectPhoto() {
        File file = openFileSelectPrompt(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        if (file != null && file.exists())
            photo = file;
    }

}
