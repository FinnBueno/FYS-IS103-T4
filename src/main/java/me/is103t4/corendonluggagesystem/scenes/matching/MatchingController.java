package me.is103t4.corendonluggagesystem.scenes.matching;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchSimilarLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.matching.RegisterMatchTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.util.Arrays;

public class MatchingController extends Controller {

    @FXML
    private TableColumn<Luggage, String> type, tag, brand, colour, characteristics, firstName, lastName, city, address;

    @FXML
    private TableView<Luggage> table;

    private Luggage baseLuggage;

    public void fill(Luggage luggage, int severity) {
        this.baseLuggage = luggage;
        FetchSimilarLuggageTask task = new FetchSimilarLuggageTask(luggage, severity);
        task.setOnSucceeded(value -> {
            Luggage[] result = (Luggage[]) task.getValue();
            ObservableList<Luggage> data = FXCollections.observableArrayList(Arrays.asList(result));
            table.setItems(data);
        });
    }

    @FXML
    public void createMatch() {
        if (table.getSelectionModel().getSelectedItem() == null)
            AlertBuilder.NO_SELECTION.showAndWait();

        Luggage select = table.getSelectionModel().getSelectedItem();
        RegisterMatchTask task = new RegisterMatchTask(baseLuggage, select);
        task.setOnSucceeded(event -> {
            if ((boolean) task.getValue()) {
                AlertBuilder.MATCH_CREATED.showTextAndWait("lostandfound@corendon.com");
            }
        });
    }

    @Override
    public boolean isOpen() {
        return false;
    }

}
