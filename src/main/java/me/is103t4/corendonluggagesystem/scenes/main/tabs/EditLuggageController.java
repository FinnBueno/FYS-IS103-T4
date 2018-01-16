package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTypesTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.UpdateLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchEditableStatussesTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.time.LocalDate;
import java.util.*;

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
    private ComboBox<String> statusBox;

    @FXML
    private Button saveButton;

    private int id = -1;
    private List<String> statusData;

    @FXML
    public void saveChanges() {
        UpdateLuggageTask task = new UpdateLuggageTask(firstNameField.getText(), lastNameField.getText(),
                addressField.getText(), cityField.getText(), zipField.getText(), phoneNumberField.getText(),
                emailField.getText(), langBox.getSelectionModel().getSelectedItem(), typeBox.getSelectionModel()
                .getSelectedIndex(), luggageIDField.getText(), brandField.getText(), toHex(colorPicker.getValue()),
                characsField.getText(), flightNumberBox.getSelectionModel().getSelectedItem(), statusBox
                .getSelectionModel().getSelectedItem(), id);
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
    public void postInit(ResourceBundle bundle) {
        // fill flights box
        FetchAirlinesTask airlinesTask = new FetchAirlinesTask(true);
        airlinesTask.setOnSucceeded(v -> flightNumberBox.getItems().addAll((List<String>) airlinesTask.getValue()));

        // fill types box
        FetchLuggageTypesTask task = new FetchLuggageTypesTask();
        task.setOnSucceeded(v -> typeBox.getItems().addAll((List<String>) task.getValue()));

        // fill status box
        FetchEditableStatussesTask statusTask = new FetchEditableStatussesTask();
        statusTask.setOnSucceeded(v -> {
            statusData = (List<String>) statusTask.getValue();
            statusBox.setItems(FXCollections.observableArrayList(statusData));
        });

        // fill lang box
        langBox.getItems().addAll(bundle.getString("english"), bundle.getString("dutch"));
    }

    public void initFields(int id, String firstName, String lastName, String address, String city, String zip, long
            phoneNumber, String email, String lang, int type, String luggageID, String brand, String color,
                           String characs, String flightNumber, String status) {
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
        if (status.equalsIgnoreCase("Handled")) {
            this.statusBox.setItems(FXCollections.observableArrayList(Collections.singletonList("Handled")));
            this.statusBox.getSelectionModel().select(0);
            this.statusBox.setDisable(true);
        } else {
            this.statusBox.setDisable(false);
            this.statusBox.setItems(FXCollections.observableArrayList(statusData));
            this.statusBox.getSelectionModel().select(status);
        }
        this.langBox.getSelectionModel().select(lang == null || lang.equalsIgnoreCase(bundle.getString("english")) ? 0 : 1);
    }

    private String toHex(Color color) {
        if (color == null)
            return null;
        return String.format("%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private Color toColor(String hex) {
        if (hex == null)
            return null;
        return Color.color(
                Integer.valueOf(hex.substring(0, 2), 16) / 255D,
                Integer.valueOf(hex.substring(2, 4), 16) / 255D,
                Integer.valueOf(hex.substring(4), 16) / 255D);
    }

    @Override
    public boolean isOpen() {
        return Tabs.OVERVIEW.isOpen(1);
    }

    public void back() {
        Tabs.OVERVIEW.setRoot(0);
    }
}
