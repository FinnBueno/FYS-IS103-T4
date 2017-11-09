/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.scenes.Scenes;

/**
 *
 * Main class and entry point of the application
 *
 * @author Finn Bon
 * @author Bas Wezenberg
 * @author Tim van Ekert
 */
public class LugSysMain extends Application {

    private Stage stage;

    @Override
    public void start(Stage st) {
	DBHandler.INSTANCE.start();
	stage = st;
	Scenes.initAll(this);

	Scene scene = new Scene(Scenes.LOGIN.getRoot(), 800, 600);
	Scenes.initScene(scene);

	stage.setScene(scene);
	stage.show();
    }

    public Stage getStage() {
	return stage;
    }

    public static void main(String[] args) {
	launch(args);
    }
}
