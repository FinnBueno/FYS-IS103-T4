package me.is103t4.corendonluggagesystem.scenes.matching;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchSimilarLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.matching.RegisterMatchTask;
import me.is103t4.corendonluggagesystem.email.EmailSender;
import me.is103t4.corendonluggagesystem.email.IEmail;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;

import java.util.Arrays;

/**
 * Controller for the matching popup
 *
 * @author Finn Bon
 */
public class MatchingController extends Controller {

    @FXML
    private TableView<Luggage> table;

    private Luggage baseLuggage;

    public void fill(Luggage luggage, int severity) {
        this.baseLuggage = luggage;
        FetchSimilarLuggageTask task = new FetchSimilarLuggageTask(luggage, severity);
        task.setOnSucceeded(value -> {
            Luggage[] result = (Luggage[]) task.getValue();
            ObservableList<Luggage> data = FXCollections.observableArrayList(Arrays.asList(result));
            table.setItems(data);
        });
    }

    @FXML
    public void createMatch() {
        if (table.getSelectionModel().getSelectedItem() == null)
            AlertBuilder.NO_SELECTION.showAndWait();

        Luggage select = table.getSelectionModel().getSelectedItem();
        RegisterMatchTask task = new RegisterMatchTask(baseLuggage, select);
        task.setOnSucceeded(event -> {
            if ((boolean) task.getValue()) {
                String address = AlertBuilder.MATCH_CREATED.showTextAndWait("lostandfound@corendon.com").orElse(null);
                if (address == null)
                    return;
                IEmail email = new IEmail("Luggage Found Notification", true, address);
                email.setContentFromURL(getClass().
                        getResource("/email/lostAndFoundNotifyEmail.html"), true).
                        setParameters(txt -> {
                            String firstName = baseLuggage.getFirstName() == null || baseLuggage.getFirstName()
                                    .length() == 0 ? select.getFirstName() : baseLuggage.getFirstName();
                            String lastName = baseLuggage.getLastName() == null || baseLuggage.getLastName().length()
                                    == 0 ? select.getLastName() : baseLuggage.getLastName();
                            String physicalAddress = baseLuggage.getAddress() == null || baseLuggage.getAddress()
                                    .length() ==
                                    0 ? select.getAddress() : baseLuggage.getAddress();
                            String city = baseLuggage.getCity() == null || baseLuggage.getCity().length() == 0 ?
                                    select.getCity() : baseLuggage.getCity();
                            String tag = baseLuggage.getTag() == null || baseLuggage.getTag().length() == 0 ? select
                                    .getTag() : baseLuggage.getTag();
                            String flight = baseLuggage.getFlight() == null || baseLuggage.getFlight().length() == 0
                                    ? select.getFlight() : baseLuggage.getFlight();
                            String type = baseLuggage.getType() == null || baseLuggage.getType().length() == 0 ?
                                    select.getType() : baseLuggage.getType();
                            String brand = baseLuggage.getBrand() == null || baseLuggage.getBrand().length() == 0 ?
                                    select.getBrand() : baseLuggage.getBrand();

                            String characteristics = baseLuggage.getCharacteristics() == null || baseLuggage.getCharacteristics().length() == 0 ?
                                    select.getCharacteristics() : baseLuggage.getCharacteristics();
                            characteristics = characteristics == null ? "" : characteristics;

                            txt = txt.replace("%%first%%", firstName);
                            txt = txt.replace("%%last%%", lastName);
                            txt = txt.replace("%%address%%", physicalAddress);
                            txt = txt.replace("%%city%%", city);
                            txt = txt.replace("%%zip%%", "");
                            txt = txt.replace("%%country%%", "");
                            txt = txt.replace("%%phone%%", "");
                            txt = txt.replace("%%email%%", address);
                            txt = txt.replace("%%lugid%%", tag == null ? "Not specified" : tag);
                            txt = txt.replace("%%flight%%", flight == null ? "Not specified" : flight);
                            txt = txt.replace("%%type%%", type == null ? "Not specified" : type);
                            txt = txt.replace("%%brand%%", select.getBrand() == null ? "Not specified" : brand);
                            txt = txt.replace("%%colour%%", select.getColour() == null ? "Not specified" : select.getColour());
                            txt = txt.replace("%%characs%%", characteristics);
                            return txt;
                        });

                EmailSender.getInstance().
                        send(email);

                notifyPassenger();
            }
        });
    }

    private void notifyPassenger() {
        String address;
        do {
            address = AlertBuilder.PASSENGER_EMAIL_PROMPT.showTextAndWait("").orElse(null);
        } while (address == null || address.length() == 0);
        IEmail email = new IEmail("Luggage Found", true, address);
        email.setContentFromURL(getClass().
                getResource("/email/luggageFoundEmail.html"), true);
        EmailSender.getInstance().
                send(email);
    }

    @Override
    public boolean isOpen() {
        return false;
    }

}
