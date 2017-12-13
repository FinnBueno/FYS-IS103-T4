package me.is103t4.corendonluggagesystem.scenes.main.tabs;

        /*
         * To change this license header, choose License Headers in Project Properties.
         * To change this template file, choose Tools | Templates
         * and open the template in the editor.
         */

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;
import me.is103t4.corendonluggagesystem.scenes.main.MainFrameController;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

/**
 * Home pagina controller aanmaken
 *
 * @author Tim van Ekert
 */
public class HomeController extends Controller {

    @Override
    public boolean isOpen() {
        return Tabs.HOME.isOpen(0);
    }

}
