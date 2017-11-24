package me.is103t4.corendonluggagesystem.scenes.main.tabs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import me.is103t4.corendonluggagesystem.scenes.Controller;

/**
 * Home pagina controller aanmaken
 *
 * @author Tim van Ekert
 */
public class HomeController extends Controller {

   @FXML
   private ImageView img;

    @Override
    public void postInit() {
	img.setPreserveRatio(true);
	img.fitWidthProperty().bind(scene.widthProperty());
	img.fitHeightProperty().bind(scene.heightProperty());
    }
    
}
