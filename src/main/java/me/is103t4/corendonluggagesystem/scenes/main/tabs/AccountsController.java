/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
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
    private TableColumn<Account, String> mailColumn;

    @FXML
    private TableColumn<Account, AccountRole> roleColumn;

    @FXML
    private TableColumn<Account, Boolean> statusColumn;

    @FXML
    private TableColumn<Account, String> phoneColumn;

    public void postInit() {

        // tell columns what values to use
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("activated"));

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
    public void addAccount(ActionEvent event) {
        Tabs.ACCOUNTS.setRoot(1);
    }

    @FXML
    public void editAccount(ActionEvent event) {
        AlertBuilder.ERROR_OCCURED.showAndWait();
    }

    @FXML
    public void changeFilter(ActionEvent event) {
        Tabs.ACCOUNTS.setRoot(2);
    }

    @FXML
    public void clearFilter(ActionEvent event) {
        AlertBuilder.ERROR_OCCURED.showAndWait();
    }

}
