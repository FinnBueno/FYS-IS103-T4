/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.tasks.accounts.ToggleActivationTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAccountsTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.filter.AccountFilterController;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author Roy Klein
 */
public class AccountsController extends Controller {

    @FXML
    private TableView<Account> table;

    @FXML
    private TableColumn<Account, String> usernameColumn;

    @FXML
    private TableColumn<Account, String> idColumn;

    @FXML
    private TableColumn<Account, String> firstNameColumn;

    @FXML
    private TableColumn<Account, String> lastNameColumn;

    @FXML
    private TableColumn<Account, String> phoneColumn;

    @FXML
    private TableColumn<Account, String> mailColumn;

    @FXML
    private TableColumn<Account, AccountRole> roleColumn;

    @FXML
    private TableColumn<Account, Boolean> activatedColumn;

    @FXML
    private Label noteLabel;

    private AccountFilterController filterController;
    private Stage accountFilterStage;

    private ObservableList<Account> tableData;

    public void postInit() {
        FetchAccountsTask task = new FetchAccountsTask();
        task.setOnSucceeded(v -> table.setItems(tableData = (ObservableList<Account>) task.getValue()));

        // load filter popup
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/filter/AccountFilter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            filterController = fxmlLoader.getController();
            filterController.initializeParentValues(this);
            accountFilterStage = new Stage();
            accountFilterStage.setTitle("Filter");
            accountFilterStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void refresh() {
        FetchAccountsTask task = new FetchAccountsTask();
        task.setOnSucceeded(v -> {
            tableData.setAll((ObservableList<Account>) task.getValue());
            clearFilter();
        });
    }

    @Override
    public boolean isOpen() {
        return Tabs.ACCOUNTS.isOpen(0);
    }

    @FXML
    public void addAccount() {
        Tabs.ACCOUNTS.setRoot(1);
    }

    @FXML
    public void editAccount() {
        Account selected = table.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;
        ((EditAccountController) Tabs.ACCOUNTS.getController(2)).setEditingAccount(selected);
        Tabs.ACCOUNTS.setRoot(2);
    }

    @FXML
    public void changeFilter() {
        if (accountFilterStage.isShowing()) {
            accountFilterStage.requestFocus();
            return;
        }
        accountFilterStage.show();
        filterController.setEditingAccount(table.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void clearFilter() {
        table.setItems(tableData);
        noteLabel.setVisible(false);
    }

    @FXML
    public void toggleActivation() {
        Account selected = table.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        ToggleActivationTask task = new ToggleActivationTask(selected);
        task.setOnSucceeded(event -> {
            boolean result = (boolean) task.getValue();
            if (result) {
                AlertBuilder.CHANGES_SAVED.showAndWait();
                refresh();
            } else AlertBuilder.ERROR_OCCURRED.showAndWait();
        });
    }

    public List<Account> getAllData() {
        return tableData;
    }

    public void setShownData(List<Account> shownData) {
        if (shownData.size() < tableData.size())
            noteLabel.setVisible(true);
        table.setItems(FXCollections.observableArrayList(shownData));
    }

    public void closeFilterWindow() {
        accountFilterStage.hide();
    }
}
