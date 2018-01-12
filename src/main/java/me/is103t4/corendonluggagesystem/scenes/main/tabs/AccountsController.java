/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.tasks.accounts.ToggleActivationTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAccountsTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

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

    public void postInit() {
        FetchAccountsTask task = new FetchAccountsTask();
        task.setOnSucceeded(v -> table.setItems((ObservableList<Account>) task.getValue()));
    }

    @FXML
    public void refresh() {
        FetchAccountsTask task = new FetchAccountsTask();
        task.setOnSucceeded(v -> table.setItems((ObservableList<Account>) task.getValue()));
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
        AlertBuilder.ERROR_OCCURRED.showAndWait();
    }

    @FXML
    public void changeFilter() {
        Tabs.ACCOUNTS.setRoot(2);
    }

    @FXML
    public void clearFilter() {
        AlertBuilder.ERROR_OCCURRED.showAndWait();
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
}
