package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchAllLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.matching.Matcher;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.filter.LuggageFilterController;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.scenes.matching.MatchingController;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LuggageOverviewController extends Controller {

    @FXML
    private TableColumn<Luggage, String> status, type, tag, brand, colour, characteristics, firstName, lastName, city, address, flight;

    @FXML
    private TableView<Luggage> table;

    @FXML
    private Label noteLabel;

    private LuggageFilterController controller;
    private ObservableList<Luggage> data;
    private Stage luggageFilterStage;

    @Override
    public void postInit() {
        FetchAllLuggageTask task = new FetchAllLuggageTask();
        task.setOnSucceeded(event -> {
            Luggage[] result = (Luggage[]) task.getValue();
            data = FXCollections.observableArrayList(Arrays.asList(result));
            table.setItems(data);
        });

        // create filter window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/filter/LuggageFilter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            controller = fxmlLoader.getController();
            controller.initializeParentValues(this);
            luggageFilterStage = new Stage();
            luggageFilterStage.setTitle("Filter");
            luggageFilterStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isOpen() {
        return Tabs.OVERVIEW.isOpen(0);
    }

    @FXML
    public void editEntry() {
        if (table.getSelectionModel().getSelectedItem() == null)
            return;

        FetchLuggageTask task = new FetchLuggageTask(table.getSelectionModel().getSelectedItem().getId());
        task.setOnSucceeded(event -> {
            Object[] set = (Object[]) task.getValue();
            ((EditLuggageController) Tabs.OVERVIEW.getController(1)).initFields((int) set[14], (String) set[0], (String) set[1],
                    (String) set[2], (String) set[3], (String) set[4], (int) set[5], (String) set[6], (String)
                            set[7], (int) set[8], (String) set[9], (String) set[10], (String) set[11], (String)
                            set[12], (String) set[13]);
            Tabs.OVERVIEW.setRoot(1);
        });
    }

    @FXML
    public void findMatch() {
        Luggage luggage = table.getSelectionModel().getSelectedItem();
        if (luggage == null)
            return;

        // prompt for search looseness
        ButtonType result = AlertBuilder.SEARCH_LOOSENESS.showAndWait().orElse(null);
        if (result == null)
            return;
        int severity;
        if (result.getText().equalsIgnoreCase("strict")) severity = 0;
        else if (result.getText().equalsIgnoreCase("loose")) severity = 2;
        else severity = 1;

        Matcher matcher = new Matcher(main.getStage(), luggage, severity);
        matcher.showMatcher();
    }

    @FXML
    public void setFilters() {
        noteLabel.setVisible(true);
        luggageFilterStage.show();
    }

    @FXML
    public void clearFilters() {
        controller.resetFilters();
        noteLabel.setVisible(false);
        table.setItems(data);

    }

    @FXML
    public void refresh() {
        clearFilters();
        FetchAllLuggageTask task = new FetchAllLuggageTask();
        task.setOnSucceeded(event -> {
            Luggage[] result = (Luggage[]) task.getValue();
            data = FXCollections.observableArrayList(Arrays.asList(result));
            table.setItems(data);
        });
    }

    public List<Luggage> getAllData() {
        return data;
    }

    public void setShownData(List<Luggage> shownData) {
        table.setItems(FXCollections.observableArrayList(shownData));
    }

    public void closeFilterWindow() {
        luggageFilterStage.hide();
    }
}
