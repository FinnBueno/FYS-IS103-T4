package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.tasks.accounts.CreateAccountTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.util.ResourceBundle;

/**
 * @author Roy Klein
 */
public class NewAccountController extends Controller {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField tagField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repasswordField;

    @FXML
    private ComboBox<String> roleBox;

    @FXML
    private TextField phoneNumberField;

    @Override
    public void postInit(ResourceBundle bundle) {
        tagField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.length() > 4)
                        newValue = newValue.substring(0, 4);
                    if (!newValue.matches("^\\d"))
                        newValue = newValue.replaceAll("[^\\d]", "");
                    tagField.setText(newValue);
                });
        phoneNumberField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.length() > 10)
                        newValue = newValue.substring(0, 10);
                    if (!newValue.matches("^\\d"))
                        newValue = newValue.replaceAll("[^\\d]", "");
                    phoneNumberField.setText(newValue);
                });

        roleBox.getItems().addAll(AccountRole.ADMIN.name(), AccountRole.MANAGERS.name(), AccountRole.EMPLOYEE.name());
    }

    @Override
    public boolean isOpen() {
        return Tabs.ACCOUNTS.isOpen(1);
    }

    @FXML
    private void createAccount() {
        if (!phoneNumberField.getText().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}\"")) {
            alert(bundle.getString("phoneInvalid"));
            return;
        }

        if (firstNameField.getText().length() < 1) {
            alert(bundle.getString("firstEmpty"));
            return;
        }

        if (lastNameField.getText().length() < 1) {
            alert(bundle.getString("lastEmpty"));
        }

        if (tagField.getText().length() != 4) {
            alert(bundle.getString("invalidTag"));
            return;
        }

        if (emailField.getText().length() < 2 || !emailField.getText().contains("@") || !emailField.getText()
                .contains(".")) {
            alert(bundle.getString("invalidEmail"));
            return;
        }

        if (passwordField.getText().length() < 8 || (passwordField.getText().matches("[0-9]+") && passwordField.getText
                ().matches("[a-z]+") && passwordField.getText().matches("[A-Z]+"))) {
            alert(bundle.getString("passRequirements"));
        }

        if (!passwordField.getText().equals(repasswordField.getText())) {
            alert(bundle.getString("noMatchPassC"));
            return;
        }

        if (!passwordField.getText().equals(repasswordField.getText())) {
            alert(bundle.getString("noMatchPassC"));
            return;
        }

        if (phoneNumberField.getLength() < 10 || !phoneNumberField.getText().matches("[0-9 +]+")) {
            alert(bundle.getString("invalidPhone"));
            return;
        }

        CreateAccountTask createAccountTask = new CreateAccountTask(firstNameField.getText(), lastNameField.getText()
                , emailField.getText(), phoneNumberField.getText(), passwordField.getText(), tagField.getText(),
                AccountRole.fromId(roleBox.getSelectionModel().getSelectedIndex()));
        createAccountTask.setOnFailed(event -> AlertBuilder.ERROR_OCCURRED.showAndWait());
        createAccountTask.setOnSucceeded(event -> {
            boolean acc = (boolean) createAccountTask.getValue();
            if (acc) {
                AlertBuilder.CHANGES_SAVED.showAndWait();
                back();
            } else
                alert(bundle.getString("userAlreadyExists"));
        });
    }

    @FXML
    public void back() {
        Tabs.ACCOUNTS.setRoot(0);
    }

}
