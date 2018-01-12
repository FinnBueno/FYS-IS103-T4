package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTypesTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.UpdateLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.util.List;
import java.util.Locale;

public class EditLuggageController extends Controller {

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
    private Button saveButton;

    private int id = -1;

    @FXML
    public void saveChanges() {
        System.out.println("Test");
        UpdateLuggageTask task = new UpdateLuggageTask(firstNameField.getText(), lastNameField.getText(),
                addressField.getText(), cityField.getText(), zipField.getText(), phoneNumberField.getText(),
                emailField.getText(), langBox.getSelectionModel().getSelectedItem(), typeBox.getSelectionModel()
                .getSelectedIndex(), luggageIDField.getText(), brandField.getText(), toHex(colorPicker.getValue()),
                characsField.getText(), flightNumberBox.getSelectionModel().getSelectedItem(), id);
        System.out.println(id);
        task.setOnSucceeded(event -> {
            if ((boolean) task.getValue()) {
                AlertBuilder.CHANGES_SAVED.showAndWait();
                ((LuggageOverviewController) Tabs.OVERVIEW.getController(0)).refresh();
                Tabs.OVERVIEW.setRoot(0);
            } else
                AlertBuilder.ERROR_OCCURRED.showAndWait();
        });

    }

    @Override
    public void postInit() {
        // fill flights box
        FetchAirlinesTask airlinesTask = new FetchAirlinesTask(true);
        airlinesTask.setOnSucceeded(v -> flightNumberBox.getItems().addAll((List<String>) airlinesTask.getValue()));

        FetchLuggageTypesTask task = new FetchLuggageTypesTask();
        task.setOnSucceeded(v -> typeBox.getItems().addAll((List<String>) task.getValue()));

        langBox.getItems().
                addAll("English", "Dutch");
    }

    public void initFields(int id, String firstName, String lastName, String address, String city, String zip, int
            phoneNumber, String email, String lang, int type, String luggageID, String brand, String color,
                           String characs, String flightNumber) {
        this.id = id;

        this.firstNameField.setText(firstName);
        this.lastNameField.setText(lastName);
        this.addressField.setText(address);
        this.cityField.setText(city);
        this.zipField.setText(zip);
        this.phoneNumberField.setText(String.valueOf(phoneNumber));
        this.emailField.setText(email);
        this.luggageIDField.setText(luggageID);

        for (int i = 0; i < flightNumberBox.getItems().size(); i++) {
            String value = flightNumberBox.getItems().get(i);
            if (flightNumber != null && value != null && value.startsWith(flightNumber)) {
                flightNumberBox.getSelectionModel().select(i);
                break;
            }
        }

        this.typeBox.getSelectionModel().select(type);
        this.brandField.setText(brand);
        this.colorPicker.setValue(toColor(color));
        this.characsField.setText(characs);
        this.langBox.getSelectionModel().select(lang == null || lang.equalsIgnoreCase("eng") ? 0 : 1);
    }

    private String toHex(Color color) {
        return String.format("%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private Color toColor(String hex) {
        return Color.color(
                Integer.valueOf(hex.substring(0, 2), 16) / 255D,
                Integer.valueOf(hex.substring(2, 4), 16) / 255D,
                Integer.valueOf(hex.substring(4), 16) / 255D);
    }

    @Override
    public boolean isOpen() {
        return Tabs.OVERVIEW.isOpen(1);
    }
}
