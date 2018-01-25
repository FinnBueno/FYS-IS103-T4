package me.is103t4.corendonluggagesystem.matching;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.scenes.matching.MatchingController;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class used to start the matching process
 *
 * @author Juan Tombal
 */
public class Matcher {

    private final int severity;
    private final Luggage luggage;
    private final ResourceBundle bundle;

    public Matcher(ResourceBundle bundle, Luggage luggage, int severity) {
        this.bundle = bundle;
        this.luggage = luggage;
        this.severity = severity;
    }

    /**
     * This method creates a new window in which the user can match luggage
     */
    public void showMatcher() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/matching/MatchingInterface.fxml"));
            fxmlLoader.setResources(bundle);
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            MatchingController controller = fxmlLoader.getController();
            controller.fill(luggage, severity);
            Stage stage = new Stage();
            stage.setTitle("Matching");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

}
