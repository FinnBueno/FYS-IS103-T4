/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.login;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.tasks.accounts.LoginTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;
import me.is103t4.corendonluggagesystem.scenes.main.KeyInputController;
import me.is103t4.corendonluggagesystem.scenes.main.MainFrameController;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;
import me.is103t4.corendonluggagesystem.util.PreferencesManager;

import java.io.IOException;
import java.util.ResourceBundle;

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
    public boolean isOpen() {
        return Scenes.LOGIN.isOpen();
    }

    @Override
    public void postInit(ResourceBundle bundle) {
        setEnterButton(loginButton);
    }

    @FXML
    private void goToRecover() {
        Scenes.PASSWORD_RECOVERY.setToScene();
    }

    @FXML
    private void login() {  
        String usernameInput = usernameField.getText();
        if (usernameInput.length() == 0 || passwordField.getText().
                length() == 0) {
            errorLabel.setText(bundle.getString("credentialsRequired"));
            return;
        }

        if (!usernameInput.contains("#") || usernameInput.length() != 9) {
            errorLabel.
                    setText(bundle.getString("usernameFormat"));
            return;
        }

        String[] split = usernameInput.split("#");
        if (split[0].length() != 4 || split[0].matches("\\d+") || split[1].length() != 4 || !split[1].matches("\\d+")) {
            errorLabel.setText(bundle.getString("usernameFormat"));
            return;
        }
        String name = split[0];
        String code = split[1];

        LoginTask task = new LoginTask(code, name, passwordField.getText());
        loginButton.setDisable(true);
        task.setOnCancelled(event -> displayErrorAlert());
        task.setOnFailed(event -> displayErrorAlert());
        task.setOnSucceeded((Event event) -> {

            Account account = (Account) task.getValue();
            Alert alert;
            if (account == null) {
                errorLabel.setText(bundle.getString("invalidLogin"));
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Dialog");
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("invalidLogin"));
                alert.showAndWait();
            } else {
                account.login();
                errorLabel.setText("");
                String sendgrid = PreferencesManager.get().get(PreferencesManager.SENDGRIDKEY);
                String dropbox = PreferencesManager.get().get(PreferencesManager.DROPBOXKEY);
                if (sendgrid == null || dropbox == null || sendgrid.length() == 0 || dropbox.length() == 0) {
                    // invalid keys
                    AlertBuilder.INVALID_KEY.showAndWait();
                    openKeyInputDialog();
                }
                MainFrameController mc = (MainFrameController) Scenes.MAIN.getController();
                mc.fillTabPane();
                Scenes.MAIN.setToScene();
            }
            usernameField.setText("");
            passwordField.setText("");

            loginButton.setDisable(false);
        });      
    }

    private void openKeyInputDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/KeyInputInterface.fxml"));
            KeyInputController controller;
            loader.setResources(bundle);
            AnchorPane pane = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(pane, 300, 150);
            Stage stage = new Stage();
            stage.setTitle("Keys");
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayErrorAlert() {
        AlertBuilder.ERROR_OCCURRED.showAndWait();
    }

}
