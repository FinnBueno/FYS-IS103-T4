/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

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
    private TextField dateOfBirthField;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Label label;

    @Override 
    public boolean isOpen() {
        return Tabs.CONFIGURATIONS.isOpen(0);
    } 
    
    
    @FXML
    private void saveChanges() {
        if (checkEmptyFields()){
            return;
        }
        
        
        
    }
    
    /**
     * check of de invoervelden niet zijn ingevuld
     * 
     * @return true wanneer field leeg is
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
//        if (dateOfBirthField.getText() == null || dateOfBirthField.getText().length() == 0) {
//            alert("Date of birth cannot be empty!");
//            return true;
//        }
        return false;
    }
}
