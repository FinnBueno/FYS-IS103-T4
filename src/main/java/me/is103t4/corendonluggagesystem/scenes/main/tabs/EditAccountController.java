package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.tasks.accounts.UpdateAccountDataTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller for the edit account (sub)tab
 *
 * @author Roy Klein
 */
public class EditAccountController extends Controller {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<AccountRole> roleBox;

    @FXML
    private ComboBox<String> activeBox;

    @FXML
    private TextField phoneNumberField;

    private Account editingAccount;

    @Override
    public void postInit(ResourceBundle bundle) {
        activeBox.setItems(FXCollections.observableList(Arrays.asList(bundle.getString("yes"), bundle.getString("no"))));
        roleBox.setItems(FXCollections.observableArrayList(AccountRole.values()));
        phoneNumberField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.length() > 10)
                        newValue = newValue.substring(0, 10);
                    if (!newValue.matches("^\\d"))
                        newValue = newValue.replaceAll("[^\\d]", "");
                    phoneNumberField.setText(newValue);
                });
    }

    public void setEditingAccount(Account acc) {
        editingAccount = acc;
        firstNameField.setText(acc.getFirstName());
        lastNameField.setText(acc.getLastName());
        emailField.setText(acc.getEmail());
        roleBox.getSelectionModel().select(acc.getRole());
        phoneNumberField.setText(acc.getPhoneNumber());
        activeBox.getSelectionModel().select(acc.isActivated() ? bundle.getString("yes") : bundle.getString("no"));
    }

    @FXML
    public void save() {
        if (editingAccount == null) {
            AlertBuilder.ERROR_OCCURRED.showAndWait();
            Tabs.ACCOUNTS.setRoot(0);
            return;
        }
        UpdateAccountDataTask task = new UpdateAccountDataTask(editingAccount.getId(), firstNameField.getText(),
                lastNameField.getText(), emailField.getText(), roleBox.getSelectionModel().getSelectedItem(),
                phoneNumberField.getText(), activeBox.getSelectionModel().getSelectedIndex() == 0);
        task.setOnSucceeded(event -> {
            AlertBuilder.CHANGES_SAVED.showAndWait();
            editingAccount = null;
            Tabs.ACCOUNTS.setRoot(0);
            ((AccountsController)Tabs.ACCOUNTS.getController(0)).refresh();
        });
    }

    @FXML
    public void back() {
        Tabs.ACCOUNTS.setRoot(0);
    }

    @Override
    public boolean isOpen() {
        return Tabs.ACCOUNTS.isOpen(2);
    }
}
