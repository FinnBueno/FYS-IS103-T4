package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import me.is103t4.corendonluggagesystem.scenes.Controller;

/**
 * DamagedLuggage
 *
 * @author imranmohmand
 */
public class DamagedLuggageController extends Controller {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField luggageIDField;
    
    @FXML
    private TextField flightNumberField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField repairCosts;
    
    @FXML
    private TextField bankinformation;

    @FXML
    private TextField damageddescription;
   
    @FXML
    private File photo;

    @FXML
    private Button registerButton;

    @Override
    public void postInit() {
        // set enter button
        setEnterButton(registerButton);
    }

    @FXML
    private void registerDamagedLuggage(){
    
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Successfully registered damaged luggage!");
        
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
