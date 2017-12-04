/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.login;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.tasks.accounts.LoginTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;
import me.is103t4.corendonluggagesystem.scenes.main.MainFrameController;

/**
 * Controller class for the login interface
 *
 * @author Finn Bon
 */
public class LoginController extends Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void goToRecover() {
        Scenes.PASSWORD_RECOVERY.setToScene();
    }

    @FXML
    private void login() {
        String usernameInput = usernameField.getText();
        if (usernameInput.length() == 0 || passwordField.getText().
                length() == 0) {
            errorLabel.setText("You must enter a username and password!");
            return;
        }

        if (!usernameInput.contains("#") || usernameInput.length() != 9) {
            errorLabel.
                    setText("A username must be formatted like 'tag#4-digit-code'");
            return;
        }

        String[] split = usernameInput.split("#");
        if (split[0].length() != 4 || split[0].matches("\\d+") || split[1].length() != 4 || !split[1].matches("\\d+")) {
            errorLabel.setText("A username must be formatted like 'tag#4-digit-code'");
            return;
        }
        String name = split[0];
        String code = split[1];

        LoginTask task = new LoginTask(code, name, passwordField.getText());
        loginButton.setDisable(true);
        task.setOnCancelled(event -> {
            loginButton.setDisable(false);
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Dialog");
            alert.setHeaderText("An error has occured!");
            alert.
                    setContentText("An unknown error has occured! Please notify the developers to make sure this error can be solved");
        });
        task.setOnFailed(event -> {
            loginButton.setDisable(false);
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Dialog");
            alert.setHeaderText("An error has occured!");
            alert.
                    setContentText("An unknown error has occured! Please notify the developers to make sure this error can be solved");
        });
        task.setOnSucceeded((Event event) -> {
            Account account = (Account) task.getValue();
            Alert alert;
            if (account == null) {
                errorLabel.setText("Invalid login!");
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Invalid credentials!");
            } else {
                account.login();
                errorLabel.setText("");
                MainFrameController mc = (MainFrameController) Scenes.MAIN.
                        getController();
                mc.fillTabPane();
                Scenes.MAIN.setToScene();
                return;
            }

            alert.showAndWait();
            loginButton.setDisable(false);
        });
    }

}
