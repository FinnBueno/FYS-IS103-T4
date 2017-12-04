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
import javafx.scene.control.Label;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

/**
 *
 * @author Sebastiaan Wezenberg
 */
public class ConfigurationsController extends Controller {
    
    @FXML
    private Label label;

    @Override
    public boolean isOpen() {
        return Tabs.CONFIGURATIONS.isOpen(0);
    }
    
}
