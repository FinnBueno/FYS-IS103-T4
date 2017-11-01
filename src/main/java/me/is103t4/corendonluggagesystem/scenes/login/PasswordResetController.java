/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.database.tasks.ChangePasswordTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;

/**
 * FXML Controller class
 *
 * @author finnb
 */
public class PasswordResetController extends Controller {

    private int code;

    @FXML
    private TextField emailField;

    @FXML
    private TextField codeField;

    @FXML
    private ProgressIndicator spinner;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repasswordField;

    @FXML
    private Button confirmButton;

    @Override
    public void postInit() {
	codeField.textProperty().
		addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
		    boolean disabled = !newValue.equals(String.valueOf(code));
		    repasswordField.setDisable(disabled);
		    passwordField.setDisable(disabled);
		    confirmButton.setDisable(disabled);
		});
	repasswordField.setDisable(true);
	passwordField.setDisable(true);
	confirmButton.setDisable(true);
    }

    @FXML
    private void backToLogin() {
	Scenes.LOGIN.setToScene();
    }

    @FXML
    private void resetPassword() {
	if (!passwordField.getText().
		equals(repasswordField.getText())) {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setHeaderText("Password error");
	    alert.setTitle("Error");
	    alert.setContentText("The passwords don't match.");
	    alert.showAndWait();
	}
	spinner.setVisible(true);
	DBTask task = new ChangePasswordTask(emailField.getText(), passwordField.
		getText());
	task.setOnSucceeded(value -> {
	    spinner.setVisible(false);
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setHeaderText("Password changed");
	    alert.setTitle("Succeeded");
	    alert.setContentText("Your password has been changed.");
	    alert.showAndWait();

	    backToLogin();
	});
	task.setOnFailed(value -> {
	    spinner.setVisible(false);
	});
	task.setOnCancelled(value -> {
	    spinner.setVisible(false);
	});
    }

    public void setRecoverInfo(int code, String address) {
	this.code = code;
	emailField.setText(address);
    }

}
