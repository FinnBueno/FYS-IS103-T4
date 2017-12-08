/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.LuggageType;
import me.is103t4.corendonluggagesystem.database.RegisterType;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.RegisterLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirportsTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchCountriesTask;
import me.is103t4.corendonluggagesystem.email.EmailSender;
import me.is103t4.corendonluggagesystem.email.IEmail;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Finn Bon
 */
public class LostLuggageController extends Controller {

    private static final String[] AIRPORTS = new String[]{
            "Amsterdam",
            "Antalya",
            "Istanbul",
            "Bodrum",
            "Dalaman",
            "Izmir",
            "Gazipasa-Alanya",
            "Nicosia-Ercan",
            "Marrakech",
            "Crete (Heraklion)",
            "Kos",
            "Rodes",
            "Zakynthos",
            "Corfu",
            "Mytilene",
            "Ohrid",
            "Samos",
            "Gran Canaria",
            "Tenerife",
            "Palma de Mallorca",
            "Malaga",
            "Fuerteventura",
            "Faro",
            "Lanzarote",
            "Hurghada",
            "Enfidha",
            "Dubai",
            "Burgas",
            "Banjul",
            "Sicily (Catania)"
    };

    @FXML
    private ComboBox<String> depAirportBox;

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
    private ComboBox<String> destBox;

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

    private List<String> airlines;

    @Override
    public boolean isOpen() {
        return Tabs.LOST_LUGGAGE.isOpen(0);
    }

    @Override
    public void postInit() {
        // set enter action
        setEnterButton(registerButton);

        // turn some textfields into numberfields
        phoneNumberField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        phoneNumberField.setText(newValue.
                                replaceAll("[^\\d]", ""));
                    }
                });

        // fill combo boxes
        FetchAirportsTask airportTask = new FetchAirportsTask();
        airportTask.setOnSucceeded(v -> {
            destBox.getItems().addAll((List<String>) airportTask.getValue());
            depAirportBox.getItems().addAll((List<String>) airportTask.getValue());
        });

        FetchCountriesTask countryTask = new FetchCountriesTask();
        countryTask.setOnSucceeded(v -> countryBox.getItems().addAll((List<String>) countryTask.getValue()));

        FetchAirlinesTask airlinesTask = new FetchAirlinesTask();
        airlinesTask.setOnSucceeded(v -> flightNumberBox.getItems().addAll((List<String>) airlinesTask.getValue()));


        typeBox.getItems().
                addAll(LuggageType.values(Locale.ENGLISH));

        langBox.getItems().
                addAll("English", "Dutch");
    }

    @FXML
    private void registerLostLuggage() {

        if (checkEmptyFields())
            return;

        RegisterLuggageTask registerLuggageTask = new RegisterLuggageTask(RegisterType.LOST, firstNameField.getText()
                , lastNameField.getText(), addressField.getText(), cityField.getText(), zipField.getText(),
                phoneNumberField.getText(), emailField.getText(), langBox.getSelectionModel().getSelectedItem(),
                LuggageType.viaLocale(typeBox
                        .getSelectionModel().getSelectedItem(), Locale.ENGLISH), luggageIDField.getText(), brandField
                .getText
                        (), colorUnknownCheckbox.isSelected() ? null : colorPicker.getValue()
                , characsField.getText(), photo, flightNumberBox.getSelectionModel().getSelectedItem(), Account.getLoggedInUser());

        registerButton.setDisable(true);
        registerLuggageTask.setOnSucceeded(v -> {
            registerButton.setDisable(false);
            IEmail email = new IEmail("Lost Luggage Confirmation", true, emailField.
                    getText());
            email.setContentFromURL(getClass().
                    getResource("/email/lostRegisteredEmail.html"), true).
                    setParameters(txt -> {
                        txt = txt.replace("%%dep%%", depAirportBox.
                                getSelectionModel().
                                getSelectedItem());
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
                                replace("%%dest%%", destBox.
                                        getSelectionModel().
                                        getSelectedItem());
                        txt = txt.
                                replace("%%type%%", (String) typeBox.
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

            promptForPDFCreation();

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
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            alert("First name cannot be empty!");
            return true;
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            alert("Last name cannot be empty!");
            return true;
        }
        if (addressField.getText() == null || addressField.getText().length() == 0) {
            alert("Address cannot be empty!");
            return true;
        }
        if (cityField.getText() == null || cityField.getText().length() == 0) {
            alert("City cannot be empty!");
            return true;
        }
        if (zipField.getText() == null || zipField.getText().length() == 0) {
            alert("ZIP code cannot be empty!");
            return true;
        }
        if (phoneNumberField.getText() == null || zipField.getText().length() == 0 || !phoneNumberField.getText().matches("\\d*")) {
            alert("Phone number is invalid!");
            return true;
        }
        if (emailField.getText() == null || emailField.getText().length() == 0) {
            alert("Email cannot be empty!");
            return true;
        }
        if (langBox.getSelectionModel().getSelectedItem() == null) {
            alert("A language must be selected!");
            return true;
        }
        if (typeBox.getSelectionModel().getSelectedItem() == null) {
            alert("Luggage type must be selected!");
            return true;
        }
        if (flightNumberBox.getSelectionModel().getSelectedItem() == null) {
            alert("Flight ID cannot be empty!");
            return true;
        }
        return false;
    }

    private void promptForPDFCreation() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Select PDF Location");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = fileChooser.showDialog(main.getStage());

        if (file != null && file.exists()) {
            // TODO: Create PDF
        }
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @FXML
    private void selectPhoto() {
        File file = openFileSelectPrompt(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        if (file != null && file.exists()) {
            photo = file;
        }
    }

}
