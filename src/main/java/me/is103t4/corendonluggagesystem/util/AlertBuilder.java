package me.is103t4.corendonluggagesystem.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AlertBuilder {

    public static final AlertBuilder REGISTERED_LUGGAGE = new AlertBuilder(Alert.AlertType.CONFIRMATION, "Luggage " +
            "registered", "Successfully registered luggage!", "You've successfully registered the luggage. Use the " +
            "buttons below to take further action. A PDF will be created. Click the button below to directly search " +
            "for matches.").addButton(new ButtonType("Search for matches", ButtonBar.ButtonData.FINISH));

    public static final AlertBuilder ERROR_OCCURED = new AlertBuilder(Alert.AlertType.ERROR, "Error", "An error has " +
            "occurred", "An error has occurred while attempting to perform this action. Please try again later!")
            .addButton(new ButtonType("OK", ButtonBar.ButtonData.FINISH));

    private final String content;
    private final String header;
    private final String title;
    private final Alert.AlertType type;
    private Set<ButtonType> types;

    public AlertBuilder(Alert.AlertType type, String title, String header, String content) {
        this.type = type;
        this.title = title;
        this.header = header;
        this.content = content;
        this.types = new HashSet<>();
    }

    private AlertBuilder addButton(ButtonType buttonType) {
        types.add(buttonType);
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

}
