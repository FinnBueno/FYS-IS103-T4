/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.LugSysMain;

/**
 * Abstract base class for every controller in the project
 *
 * @author Finn Bon
 */
public abstract class Controller implements Initializable {

    protected Scene scene;
    protected LugSysMain main;
    private Button clickButton;

    public abstract boolean isOpen();

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setMain(LugSysMain main) {
        this.main = main;
    }

    public void init() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    if (event.getCode() == KeyCode.ENTER && clickButton != null && isOpen())
                        clickButton.fire();
                });

        postInit();
    }

    public void postInit() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    protected void setEnterButton(Button button) {
        clickButton = button;
    }



    protected void notify(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText("Beep!");
        alert.setContentText(s);
        alert.showAndWait();
    }

    protected void alert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input error");
        alert.setHeaderText("Something went wrong!");
        alert.setContentText(s);
        alert.showAndWait();
    }

    protected File openFileSelectPrompt(FileChooser.ExtensionFilter... extensionFilters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().
                addAll(extensionFilters);
        return fileChooser.showOpenDialog(main.getStage());
    }

}
