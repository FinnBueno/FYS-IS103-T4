/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.email.EmailSender;
import me.is103t4.corendonluggagesystem.email.IEmail;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

import java.io.File;

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

    private static final String[] COUNTRIES = new String[]{
            "Afghanistan",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei Darussalam",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cabo Verde",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo",
            "Costa Rica",
            "Côte d'Ivoire",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czech Republic",
            "Democratic People's Republic of Korea (North Korea)",
            "Democratic Republic of the Cong",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Ethiopia",
            "Fiji",
            "Finland",
            "France",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Greece",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Kuwait",
            "Kyrgyzstan",
            "Lao People's Democratic Republic (Laos)",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Macedonia",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia (Federated States of)",
            "Monaco",
            "Mongolia",
            "Montenegro",
            "Morocco",
            "Mozambique",
            "Myanmar",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Republic of Korea (South Korea)",
            "Republic of Moldova",
            "Romania",
            "Russian Federation",
            "Rwanda",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South Sudan",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Swaziland",
            "Sweden",
            "Switzerland",
            "Syrian Arab Republic",
            "Tajikistan",
            "Thailand",
            "Timor-Leste",
            "Togo",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom of Great Britain and Northern Ireland",
            "United Republic of Tanzania",
            "United States of America",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Zambia",
            "Zimbabwe"
    };

    @FXML
    private ComboBox depAirportBox;

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
    private ComboBox countryBox;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField luggageIDField;

    @FXML
    private TextField flightNumberField;

    @FXML
    private ComboBox destBox;

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
    private ComboBox langBox;

    @FXML
    private Button registerButton;

    @FXML
    private File photo;

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
        depAirportBox.getItems().
                addAll(AIRPORTS);

        countryBox.getItems().
                addAll(COUNTRIES);

        destBox.getItems().
                addAll(AIRPORTS);

        typeBox.getItems().
                addAll("Suitcase", "Handbag");

        langBox.getItems().
                addAll("English", "Dutch");
    }

    @FXML
    private void registerLostLuggage() {

        IEmail email = new IEmail("Lost Luggage Confirmation", true, emailField.
                getText());
        email.setContentFromURL(getClass().
                getResource("/email/lostRegisteredEmail.html"), true).
                setParameters(txt -> {
                    if (txt == null)
                        System.out.println("Nani");
                    txt = txt.replace("%%dep%%", (String) depAirportBox.
                            getSelectionModel().
                            getSelectedItem());
                    txt = txt.replace("%%first%%", firstNameField.getText());
                    txt = txt.replace("%%last%%", lastNameField.getText());
                    txt = txt.replace("%%address%%", addressField.getText());
                    txt = txt.replace("%%city%%", cityField.getText());
                    txt = txt.replace("%%zip%%", zipField.getText());
                    txt = txt.replace("%%country%%", (String) countryBox.
                            getSelectionModel().
                            getSelectedItem());
                    txt = txt.replace("%%phone%%", phoneNumberField.getText());
                    txt = txt.replace("%%email%%", emailField.getText());
                    txt = txt.replace("%%lugid%%", luggageIDField.getText());
                    txt = txt.replace("%%flight%%", flightNumberField.getText());
                    txt = txt.
                            replace("%%dest%%", (String) destBox.
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

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Successfully sent email to passenger!");
        alert.
                setContentText("An email has been sent to the passenger regarding their lost luggage.");

        alert.showAndWait();
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
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

        if (file != null && file.exists()) {
            photo = file;
        }
    }

}
