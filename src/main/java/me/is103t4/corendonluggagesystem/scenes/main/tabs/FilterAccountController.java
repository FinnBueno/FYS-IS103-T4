/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

import java.awt.*;

/**
 * FXML Controller class
 *
 * @author Roy
 */
public class FilterAccountController extends Controller {

    @FXML
    private TextField username;

    @FXML
    private TextField tagField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> roleBox;

    @FXML
    private ComboBox<String> activeBox;

    @Override
    public boolean isOpen() {
        return Tabs.ACCOUNTS.isOpen(2);
    }
    
}

