/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.login;

import javafx.animation.FadeTransition;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.util.Duration;
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
		    if (disabled) {
			FadeTransition fadeOut = new FadeTransition(Duration.
				millis(250), repasswordField);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0.1);
			fadeOut.setOnFinished(value -> {
			    repasswordField.setDisable(true);
			    repasswordField.setText("");
			});
			fadeOut.play();

			fadeOut = new FadeTransition(Duration.
				millis(250), confirmButton);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0.1);
			fadeOut.setOnFinished(value -> {
			    confirmButton.setDisable(true);
			});
			fadeOut.play();

			fadeOut = new FadeTransition(Duration.
				millis(250), passwordField);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0.1);
			fadeOut.setOnFinished(value -> {
			    passwordField.setDisable(true);
			    passwordField.setText("");
			});
			fadeOut.play();
		    } else {
			FadeTransition fadeOut = new FadeTransition(Duration.
				millis(250), repasswordField);
			fadeOut.setFromValue(0.1);
			fadeOut.setToValue(1);
			fadeOut.setOnFinished(value -> {
			    repasswordField.setDisable(false);
			});
			fadeOut.play();

			fadeOut = new FadeTransition(Duration.
				millis(250), confirmButton);
			fadeOut.setFromValue(0.1);
			fadeOut.setToValue(1);
			fadeOut.setOnFinished(value -> {
			    confirmButton.setDisable(false);
			});
			fadeOut.play();
			
			fadeOut = new FadeTransition(Duration.
				millis(250), passwordField);
			fadeOut.setFromValue(0.1);
			fadeOut.setToValue(1);
			fadeOut.setOnFinished(value -> {
			    passwordField.setDisable(false);
			});
			fadeOut.play();
		    }
		});
	FadeTransition fadeOut = new FadeTransition(Duration.
		millis(250), repasswordField);
	fadeOut.setFromValue(1);
	fadeOut.setToValue(0.1);
	fadeOut.setOnFinished(value -> {
	    repasswordField.setDisable(true);
	    repasswordField.setText("");
	});
	fadeOut.play();

	fadeOut = new FadeTransition(Duration.
		millis(250), confirmButton);
	fadeOut.setFromValue(1);
	fadeOut.setToValue(0.1);
	fadeOut.setOnFinished(value -> {
	    confirmButton.setDisable(true);
	});
	fadeOut.play();

	fadeOut = new FadeTransition(Duration.
		millis(250), passwordField);
	fadeOut.setFromValue(1);
	fadeOut.setToValue(0.1);
	fadeOut.setOnFinished(value -> {
	    passwordField.setDisable(true);
	    passwordField.setText("");
	});
	fadeOut.play();
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
