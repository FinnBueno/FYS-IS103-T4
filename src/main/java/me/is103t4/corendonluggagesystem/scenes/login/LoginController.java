/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.tasks.LoginTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;

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

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
	errorLabel.setWrapText(true);
    }
    
    @FXML
    private void login() {
	String usernameInput = usernameField.getText();
	if (usernameInput.length() == 0 || passwordField.getText().
		length() == 0) {
	    errorLabel.setText("You must enter a username and password!");
	    errorLabel.setWrapText(true);
	    return;
	}

	if (!usernameInput.contains("#") || usernameInput.length() != 9) {
	    errorLabel.
		    setText("A username must be formatted like 'tag#4-digit-code'");
	    errorLabel.setWrapText(true);
	    return;
	}

	String[] split = usernameInput.split("#");
	String name = split[0];
	int code;
	try {
	    code = Integer.parseInt(split[1]);
	} catch (NumberFormatException ex) {
	    errorLabel.
		    setText("A username must be formatted like 'tag#4-digit-code'");
	    errorLabel.setWrapText(true);
	    return;
	}

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
	task.setOnSucceeded(event -> {
	    Account account = (Account) task.getValue();
	    Alert alert;
	    if (account == null) {
		alert = new Alert(AlertType.WARNING);
		alert.setTitle("Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Invalid credentials!");
	    } else {
		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Dialog");
		alert.setHeaderText("Logged in succesfully!");
		alert.
			setContentText("Code: " + account.getCode() + "\nUser: " + account.
				getUsername() + "\nEmail: " + account.getEmail() + "\nRole: " + account.
				getRole().
				name());
	    }

	    alert.showAndWait();
	    loginButton.setDisable(false);
	});
    }

}
