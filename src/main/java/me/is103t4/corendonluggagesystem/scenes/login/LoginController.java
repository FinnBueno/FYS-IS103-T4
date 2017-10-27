/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.database.DBHandler;
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

    @FXML
    private void login() {
	DBHandler db = DBHandler.INSTANCE;

	String usernameInput = usernameField.getText();
	if (usernameInput.length() == 0 || passwordField.getText().
		length() == 0) {
	    // no input
	    return;
	}

	if (!usernameInput.contains("#") || usernameInput.length() != 9) {
	    // incorrect format
	    return;
	}

	String[] split = usernameInput.split("#");
	String name = split[0];
	int code;
	try {
	    code = Integer.parseInt(split[1]);
	} catch (NumberFormatException ex) {
	    // incorrect format
	    return;
	}

	LoginTask task = new LoginTask(code, name, passwordField.getText());
	task.setOnSucceeded(event -> {
	    
	});
    }

}
