/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import me.is103t4.corendonluggagesystem.LugSysMain;
import me.is103t4.corendonluggagesystem.scenes.login.LoginController;

/**
 * An enumeration holding all the possible scenes that can be displayed on the primary
 * stage
 *
 * @author Finn Bon
 */
public enum Scenes {

    LOGIN("Login"),
    PASSWORD_RECOVERY("RecoverPassword"),
    RECOVERY_CONFIRMATION("PasswordReset"),
    MAIN("MainFrame");

    private Pane root;
    private Controller controller;
    private final String fxmlURL;

    Scenes(String name) {
        fxmlURL = "/fxml/" + name + "Interface.fxml";
    }

    public void initialize(LugSysMain main) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource(fxmlURL));

            root = loader.load();
            controller = loader.getController();
            if (controller != null) {
                controller.setScene(scene);
                controller.setMain(main);
                controller.init();
                if (controller instanceof LoginController)
                    controller.postInit();
            }
        } catch (IOException ex) {
            Logger.getLogger(Scenes.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public Controller getController() {
        return controller;
    }

    public Pane getRoot() {
        return root;
    }

    public void setToScene() {
        scene.setRoot(root);
    }

    private static Scene scene;

    public static void initScene(Scene scene) {
        Scenes.scene = scene;
    }

    public static void initAll(LugSysMain lugSysMain) {
        for (Scenes scene : values()) {
            scene.initialize(lugSysMain);
        }
    }

    public boolean isOpen() {
        return scene.getRoot() == root;
    }
}
