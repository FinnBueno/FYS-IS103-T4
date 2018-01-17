package me.is103t4.corendonluggagesystem.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static javafx.scene.control.ButtonBar.*;

public class AlertBuilder {

    private static final ResourceBundle bundle;
    static {
        String lang = PreferencesManager.get().get(PreferencesManager.LANGUAGE);
        Locale locale = lang == null || !lang.equalsIgnoreCase("NL") ? new Locale("en", "US")
                : new Locale("nl", "NL");
        URL url = AlertBuilder.class.getResource("/language/");
        ClassLoader loader = new URLClassLoader(new URL[]{url});
        bundle = ResourceBundle.getBundle("bundle", locale, loader);
    }

    public static final AlertBuilder LOADING = new AlertBuilder(Alert.AlertType.INFORMATION,
            bundle.getString("waiting"),
            bundle.getString("waitingH"),
            bundle.getString("waitingC"));

    public static final AlertBuilder INVALID_DROPBOX = new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("error"),
            bundle.getString("error"),
            bundle.getString("invalidDropbox"));

    public static final AlertBuilder INVALID_SENDGRID= new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("error"),
            bundle.getString("error"),
            bundle.getString("invalidSendgrid"));

    public static final AlertBuilder INVALID_KEY = new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("error"),
            bundle.getString("error"),
            bundle.getString("invalidKey"))
            .addButton(new ButtonType("OK", ButtonData.FINISH));

    public static final AlertBuilder REGISTERED_LUGGAGE = new AlertBuilder(Alert.AlertType.CONFIRMATION,
            bundle.getString("luggageRegistered"),
            bundle.getString("luggageRegisteredH"),
            bundle.getString("luggageRegisteredC"))
            .addButton(new ButtonType(bundle.getString("findMatch"), ButtonData.FINISH));

    public static final AlertBuilder ERROR_OCCURRED = new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("error"),
            bundle.getString("errorH"),
            bundle.getString("errorC"))
            .addButton(new ButtonType("OK", ButtonData.FINISH));

    public static final AlertBuilder CHANGES_SAVED = new AlertBuilder(Alert.AlertType.CONFIRMATION,
            bundle.getString("saved"),
            bundle.getString("savedH"),
            bundle.getString("savedC"))
            .addButton(new ButtonType("OK", ButtonData.FINISH));

    public static final AlertBuilder SEARCH_LOOSENESS = new AlertBuilder(Alert.AlertType.INFORMATION,
            bundle.getString("matching"),
            bundle.getString("matchingH"),
            bundle.getString("matchingC"))
            .addButton(new ButtonType(bundle.getString("strict"), ButtonData.CANCEL_CLOSE))
            .addButton(new ButtonType(bundle.getString("normal"), ButtonData.OTHER))
            .addButton(new ButtonType(bundle.getString("loose"), ButtonData.FINISH));

    public static final AlertBuilder NO_SELECTION = new AlertBuilder(Alert.AlertType.WARNING,
            bundle.getString("select"),
            bundle.getString("selectH"),
            bundle.getString("selectC"))
            .addButton(new ButtonType("OK", ButtonData.OK_DONE));

    public static final AlertBuilder MATCH_CREATED = new AlertBuilder(Alert.AlertType.INFORMATION,
            bundle.getString("match"),
            bundle.getString("matchH"),
            bundle.getString("matchC"))
            .addTextField();

    public static final AlertBuilder PASSENGER_EMAIL_PROMPT = new AlertBuilder(Alert.AlertType.INFORMATION,
            bundle.getString("passEmail"),
            bundle.getString("passEmailH"),
            bundle.getString("passEmailC")).addTextField().addButton(new
            ButtonType("Done", ButtonData.OK_DONE));

    public static final AlertBuilder NO_PERMISSION = new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("noPerm"),
            bundle.getString("noPermH"),
            bundle.getString("noPermC"))
            .addButton(new ButtonType("OK", ButtonData.OK_DONE));

    public static final AlertBuilder NO_IMAGE = new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("noImage"),
            bundle.getString("noImageH"),
            bundle.getString("noImageC"))
            .addButton(new ButtonType("OK", ButtonData.OK_DONE));

    public static final AlertBuilder NOT_ALL_REQUIRED_FILLED = new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("notAllRequiredFields"),
            bundle.getString("notAllRequiredFieldsH"),
            bundle.getString("notAllRequiredFieldsC"))
            .addButton(new ButtonType("OK", ButtonData.OK_DONE));

    public static final AlertBuilder RESTART_PROMPT = new AlertBuilder(Alert.AlertType.INFORMATION,
            bundle.getString("restart"),
            bundle.getString("restartH"),
            bundle.getString("restartC"))
            .addButton(new ButtonType("Yes", ButtonData.YES))
            .addButton(new ButtonType("No", ButtonData.NO));

    public static final AlertBuilder NO_MATCH_PASSWORDS = new AlertBuilder(Alert.AlertType.ERROR,
            bundle.getString("noPassMatch"),
            bundle.getString("noPassMatchH"),
            bundle.getString("noPassMatchC"))
            .addButton(new ButtonType("OK", ButtonData.OK_DONE));

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

    public AlertBuilder addButton(ButtonType buttonType) {
        types.add(buttonType);
        return this;
    }

    private AlertBuilder addTextField() {
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

    public Alert show() {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.NONE);
        alert.show();
        return alert;
    }
}
