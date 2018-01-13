/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.scenes.Scenes;

/**
 * Main class and entry point of the application
 *
 * @author Finn Bon
 */
public class LugSysMain extends Application {

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage st) {
        DBHandler.INSTANCE.start();
        stage = st;

        scene = new Scene(new Pane(), 800, 600);

        Scenes.initScene(scene);
        Scenes.initAll(this);

        scene.setRoot(Scenes.LOGIN.getRoot());

        stage.setOnHidden(event -> Platform.exit());

        // set icon and name
        stage.setTitle("Corendon - Luggage Detective");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Square Logo.png")));

        stage.setScene(scene);
        stage.show();

        stage.requestFocus();

        stage.setOnCloseRequest(event -> DBHandler.INSTANCE.close());
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
