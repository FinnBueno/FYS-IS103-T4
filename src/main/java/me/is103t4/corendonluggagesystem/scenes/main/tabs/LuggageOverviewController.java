package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.fxml.FXML;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

public class LuggageOverviewController extends Controller {

    @Override
    public boolean isOpen() {
        return Tabs.OVERVIEW.isOpen(0);
    }

    @FXML
    public void viewEntry() {
        AlertBuilder.ERROR_OCCURED.showAndWait();
    }

    @FXML
    public void editEntry() {
        AlertBuilder.ERROR_OCCURED.showAndWait();
    }

    @FXML
    public void findMatch() {
        AlertBuilder.ERROR_OCCURED.showAndWait();
    }

    @FXML
    public void setFilters() {
        AlertBuilder.ERROR_OCCURED.showAndWait();
    }

    @FXML
    public void clearFilters() {
        AlertBuilder.ERROR_OCCURED.showAndWait();
    }

}
