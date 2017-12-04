/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

/**
 * FXML Controller class
 *
 * @author timvanekert
 */
public class FoundLuggageController extends Controller {
    
    @FXML
    private ComboBox depAirportBox;
    
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField luggageIDField;

    @FXML
    private TextField flightNumberField;

    @FXML
    private ComboBox destBox;
    
     @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TextField brandField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private CheckBox colorUnknownCheckbox;

    @FXML
    private TextField characsField;
    
    @FXML
    private File photo;
    
    @FXML
    private Button registerButton;

    @Override
    public boolean isOpen() {
        return Tabs.FOUND_LUGGAGE.isOpen(0);
    }

    @Override
    public void postInit() {
        // set enter button
        setEnterButton(registerButton);
    }

    @FXML
    private void registerFoundLuggage(){
    
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Successfully registered found luggage!");
        
        alert.showAndWait();
}
    
    
    @FXML
    private void selectPhoto() {
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Select Photo File");
	fileChooser.setInitialDirectory(
		new File(System.getProperty("user.home"))
	);
	fileChooser.getExtensionFilters().
		addAll(
			new FileChooser.ExtensionFilter("All Files", "*.*"),
			new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
			new FileChooser.ExtensionFilter("PNG", "*.png")
		);
	File file = fileChooser.showOpenDialog(main.getStage());

	if (file != null && file.exists()) {
	    photo = file;
	}
    }
}
