/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
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

    public void setScene(Scene scene) {
	this.scene = scene;
    }

    public void setMain(LugSysMain main) {
	this.main = main;
    }

    public void postInit() {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        scene.setOnKeyTyped(value -> {
            if (value.getCode() == KeyCode.ENTER)
                clickButton.fire();
        });
    }

    protected void setEnterButton(Button button) {
        clickButton = button;
    }

}
