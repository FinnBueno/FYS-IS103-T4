/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.PreferencesManager;

/**
 *
 * @author Sebastiaan Wezenberg
 */
public class ConfigurationsController extends Controller {
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField phoneNumberField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField sendgridKeyField;

    @Override 
    public boolean isOpen() {
        return Tabs.CONFIGURATIONS.isOpen(0);
    } 
    
    @Override
    public void postInit() {
        // turn some textfields into numberfields
        phoneNumberField.textProperty().
                addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        phoneNumberField.setText(newValue.
                                replaceAll("[^\\d]", ""));
                    }
                });
        sendgridKeyField.setText(PreferencesManager.get().get(PreferencesManager.SENDGRIDKEY));
    }
    
    @FXML
    public void saveChanges() {
        if (checkEmptyFields()){
            return;
        }      
        PreferencesManager.get().set(PreferencesManager.SENDGRIDKEY, sendgridKeyField.getText());         
    }
    
    /**
     * check if input fields are not empty
     * 
     * @return true when fields are empty
     */
    private boolean checkEmptyFields() {
 
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            alert("First name cannot be empty!");
            return true;
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            alert("Last name cannot be empty!");
            return true;
        }
        if (phoneNumberField.getText() == null || phoneNumberField.getText().length() == 0) {
            alert("Phone number cannot be empty!");                                          
            return true;
        }
        if (emailField.getText() == null || emailField.getText().length() == 0) {
            alert("Email cannot be empty!");
            return true;
        }     
        if (sendgridKeyField.getText() == null || emailField.getText().length() == 0) {
            alert("sendgrid key cannot be empty!");
            return true;
        }
        return false;
    }
}
