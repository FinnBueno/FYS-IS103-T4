package me.is103t4.corendonluggagesystem.scenes.filter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTypesTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.tabs.LuggageOverviewController;
import me.is103t4.corendonluggagesystem.util.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LuggageFilterController extends Controller {

    private static final double COLOR_THRESHOLD = 150;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<String> flightBox;

    @FXML
    private ComboBox<String> statusBox;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TextField tagField;

    @FXML
    private TextField brandField;

    @FXML
    private ColorPicker colorPicker;
    @FXML
    private CheckBox noColor;

    @FXML
    private TextField characteristics;

    @FXML
    private Button applyButton;

    private LuggageOverviewController overview;

    public void initializeParentValues(LuggageOverviewController overview) {
        setEnterButton(applyButton);

        this.overview = overview;
        this.noColor.setSelected(true);

        // load values from database
        DBTask task;
        task = new FetchAirlinesTask();
        DBTask finalTask1 = task;
        task.setOnSucceeded(event -> flightBox.setItems(FXCollections.observableArrayList((List<String>)
                finalTask1.getValue())));

        task = new FetchLuggageTypesTask();
        DBTask finalTask = task;
        task.setOnSucceeded(event -> typeBox.setItems(FXCollections.observableArrayList((List<String>) finalTask
                .getValue())));

        statusBox.setItems(FXCollections.observableArrayList(Arrays.asList("Lost", "Found")));
    }

    public void resetFilters() {
        firstNameField.setText("");
        lastNameField.setText("");
        cityField.setText("");
        addressField.setText("");
        flightBox.getSelectionModel().clearSelection();
        statusBox.getSelectionModel().clearSelection();
        typeBox.getSelectionModel().clearSelection();
        tagField.setText("");
        brandField.setText("");
        colorPicker.setValue(Color.WHITE);
        noColor.setSelected(true);
        characteristics.setText("");
    }

    @FXML
    public void applyFilters() {
        List<Luggage> data = new ArrayList<>(overview.getAllData());
        data.removeIf(lug -> !canGoThroughFilters(lug));
        overview.setShownData(data);
        overview.closeFilterWindow();
    }

    private boolean canGoThroughFilters(Luggage lug) {
        // TODO: Fix
        if (checkValue(firstNameField, lug.getFirstName()))
            return false;
        if (checkValue(lastNameField, lug.getLastName()))
            return false;
        if (checkValue(cityField, lug.getCity()))
            return false;
        if (checkValue(addressField, lug.getAddress()))
            return false;
        if (checkValue(tagField, lug.getTag()))
            return false;
        if (checkValue(brandField, lug.getBrand()))
            return false;
        if (checkValue(characteristics, lug.getCharacteristics()))
            return false;
        if (checkComboboxValue(flightBox.getSelectionModel().getSelectedItem(), lug.getFlight()))
            return false;
        if (statusBox.getSelectionModel().getSelectedItem() != null && !lug.getStatus().equalsIgnoreCase(statusBox
                .getSelectionModel().getSelectedItem()))
            return false;
        if (typeBox.getSelectionModel().getSelectedItem() != null && !lug.getType().equalsIgnoreCase(typeBox
                .getSelectionModel().getSelectedItem()))
            return false;
        if (noColor.isSelected())
            return true;
        if (ColorUtil.getDistance(colorPicker.getValue(), lug.getColour()) < COLOR_THRESHOLD)
            return true;
        return false;
    }

    private boolean checkComboboxValue(String selectedItem, String flight) {
        // flightBox.getSelectionModel().getSelectedItem() != null && !lug.getFlight().equalsIgnoreCase(flightBox.getSelectionModel().getSelectedItem())
        selectedItem = selectedItem == null || selectedItem.length() == 0 ? "" : selectedItem.split(" - ")[0];
        flight = flight == null || flight.length() == 0 ? "" : flight;
        return selectedItem.length() != 0 && !selectedItem.equalsIgnoreCase(flight);
    }

    private boolean checkValue(TextField field, String text) {
        String fieldText = field.getText() == null ? "" : field.getText().toLowerCase();
        text = text == null ? "" : text.toLowerCase();
        return !text.contains(fieldText);
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
