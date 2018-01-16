package me.is103t4.corendonluggagesystem.scenes.main;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;
import me.is103t4.corendonluggagesystem.util.PreferencesManager;

public class KeyInputController extends Controller {

    @FXML
    private PasswordField sendgrid;

    @FXML
    private PasswordField dropbox;
    private Stage stage;

    @Override
    public boolean isOpen() {
        return false;
    }

    @FXML
    private void save() {
        PreferencesManager.get().set(PreferencesManager.SENDGRIDKEY, sendgrid.getText());
        PreferencesManager.get().set(PreferencesManager.DROPBOXKEY, dropbox.getText());
        AlertBuilder.CHANGES_SAVED.showAndWait();
        stage.hide();
    }

    public void setStage(Stage st) {
        stage = st;
    }

}
