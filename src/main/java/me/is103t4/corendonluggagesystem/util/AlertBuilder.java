package me.is103t4.corendonluggagesystem.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static javafx.scene.control.ButtonBar.*;

public class AlertBuilder {

    public static final AlertBuilder REGISTERED_LUGGAGE = new AlertBuilder(Alert.AlertType.CONFIRMATION, "Luggage " +
            "registered", "Successfully registered luggage!", "You've successfully registered the luggage. Use the " +
            "buttons below to take further action. A PDF will be created. Click the button below to directly search " +
            "for matches.").addButton(new ButtonType("Search for matches", ButtonData.FINISH));
    public static final AlertBuilder ERROR_OCCURRED = new AlertBuilder(Alert.AlertType.ERROR, "Error", "An error has " +
            "occurred", "An error has occurred while attempting to perform this action. Please try again later!")
            .addButton(new ButtonType("OK", ButtonData.FINISH));
    public static final AlertBuilder CHANGES_SAVED = new AlertBuilder(Alert.AlertType.CONFIRMATION, "Saved", "Changes" +
            " saved",
            "Your changes have been saved!")
            .addButton(new ButtonType("OK", ButtonData.FINISH));
    public static final AlertBuilder SEARCH_LOOSENESS = new AlertBuilder(Alert.AlertType.INFORMATION, "Matching",
            "Select Matching Level", "Please select how loose the matching should be.")
            .addButton(new ButtonType("Strict", ButtonData.CANCEL_CLOSE))
            .addButton(new ButtonType("Normal", ButtonData.OTHER))
            .addButton(new ButtonType("Loose", ButtonData.FINISH));
    public static final AlertBuilder NO_SELECTION = new AlertBuilder(Alert.AlertType.WARNING, "Alert!", "No " +
            "Selection!", "You must make a " +
            "selection first!").addButton(new ButtonType("OK", ButtonData.OK_DONE));
    public static final AlertBuilder MATCH_CREATED = new AlertBuilder(Alert.AlertType.INFORMATION, "Success!", "Match" +
            " created",
            "A match has been created. The owner will be notified their luggage has been found. Please enter the " +
                    "e-mail address of the lost-and-found service so they can be asked to ship the luggage.")
            .addTextField();
    public static final AlertBuilder PASSENGER_EMAIL_PROMPT = new AlertBuilder(Alert.AlertType.INFORMATION,
            "Passenger email", "Passenger email", "Please enter the passenger's email.").addTextField().addButton(new
            ButtonType("Done", ButtonData.OK_DONE));
    public static final AlertBuilder NO_PERMISSION = new AlertBuilder(Alert.AlertType.ERROR,
            "No permission", "No permission", "You do not have the right role to perform this action").addButton(new
            ButtonType("OK", ButtonData.OK_DONE));
    public static final AlertBuilder NO_IMAGE = new AlertBuilder(Alert.AlertType.ERROR,"Error", "No Image", "No image was submitted for " +
            "this entry").addButton(new ButtonType("OK", ButtonData.OK_DONE));
    public static final AlertBuilder NOT_ALL_REQUIRED_FILLED = new AlertBuilder(Alert.AlertType.ERROR, "Error", "Empty fields",
            "Not all required fields have been filled in!").addButton(new ButtonType("OK", ButtonData.OK_DONE));

    private final String content;
    private final String header;
    private final String title;
    private final Alert.AlertType type;
    private Set<ButtonType> types;
    private boolean text;

    public AlertBuilder(Alert.AlertType type, String title, String header, String content) {
        this.type = type;
        this.title = title;
        this.header = header;
        this.content = content;
        this.text = false;
        this.types = new HashSet<>();
    }

    private AlertBuilder addButton(ButtonType buttonType) {
        types.add(buttonType);
        return this;
    }

    private AlertBuilder addTextField() {
        text = true;
        return this;
    }


    public Optional<ButtonType> showAndWait() {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.NONE);
        alert.getButtonTypes().setAll(types);
        return alert.showAndWait();
    }

    public Optional<String> showTextAndWait(String defaultValue) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.getEditor().setText(defaultValue);
        return dialog.showAndWait();
    }

}
