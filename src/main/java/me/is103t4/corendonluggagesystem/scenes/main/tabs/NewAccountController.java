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
    public void postInit() {
        tagField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (!newValue.matches("^\\d{1,4}")) {
                        tagField.setText(newValue.
                                replaceAll("[^\\d]", "").substring(0, 4));
                    }
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
            alert("The phone number is invalid!");
            return;
        }

        if (firstNameField.getText().length() < 1) {
            alert("The first name can't be empty!");
            return;
        }

        if (lastNameField.getText().length() < 1) {
            alert("The last name can't be empty!");
        }

        if (tagField.getText().length() != 4) {
            alert("The tag must be a 4 digit numeric code!");
            return;
        }

        if (emailField.getText().length() < 2 || !emailField.getText().contains("@") || !emailField.getText()
                .contains(".")) {
            alert("The email is invalid!");
            return;
        }

        if (passwordField.getText().length() < 8 || (passwordField.getText().matches("[0-9]+") && passwordField.getText
                ().matches("[a-z]+") && passwordField.getText().matches("[A-Z]+"))) {
            alert("A password must be at least 8 characters long, contain a number, one uppercase and one lowercase " +
                    "letter.");
        }

        if (!passwordField.getText().equals(repasswordField.getText())) {
            alert("The passwords don't match!");
            return;
        }

        if (!passwordField.getText().equals(repasswordField.getText())) {
            alert("The passwords don't match!");
            return;
        }

        if (phoneNumberField.getLength() < 10 || !phoneNumberField.getText().matches("[0-9 +]+")) {
            alert("The phone number is invalid!");
            return;
        }

        CreateAccountTask createAccountTask = new CreateAccountTask(firstNameField.getText(), lastNameField.getText()
                , emailField.getText(), phoneNumberField.getText(), passwordField.getText(), tagField.getText(),
                AccountRole.fromId(roleBox.getSelectionModel().getSelectedIndex()));
        createAccountTask.setOnFailed(event -> alert("The account could not be created due to an error."));
        createAccountTask.setOnSucceeded(event -> {
            boolean acc = (boolean) createAccountTask.getValue();
            if (acc) {
                notify("The account has been created!");
                Tabs.ACCOUNTS.setRoot(0);
            } else
                alert("The tag and username combination or email already exists!");
        });
    }

}
