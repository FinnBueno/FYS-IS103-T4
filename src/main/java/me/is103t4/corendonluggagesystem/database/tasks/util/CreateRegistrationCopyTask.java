package me.is103t4.corendonluggagesystem.database.tasks.util;

import javafx.application.Platform;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.pdf.PDF;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CreateRegistrationCopyTask extends DBTask {

    private final File[] otherFiles;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String country;
    private String phoneNumber;
    private String email;
    private String luggageId;
    private String flight;
    private String type;
    private String brand;
    private String colour;
    private String characteristics;
    private String language;
    private final Stage stage;
    private final Luggage luggage;
    private int registrationID;
    private String employee;

    public CreateRegistrationCopyTask(String firstName, String lastName, String address, String city, String zip,
                                      String country, String phoneNumber, String email, String luggageId, String
                                              flight, String type, String brand, String colour, String
                                              characteristics, String language, Stage stage, File[] files) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.luggageId = luggageId;
        this.flight = flight;
        this.type = type;
        this.brand = brand;
        this.colour = colour;
        this.characteristics = characteristics;
        this.language = language;
        this.stage = stage;
        this.otherFiles = files;

        this.luggage = null;

        start();
    }

    public CreateRegistrationCopyTask(Luggage l, Stage stage) {
        this.firstName = null;
        this.lastName = null;
        this.address = null;
        this.city = null;
        this.zip = null;
        this.country = null;
        this.phoneNumber = null;
        this.email = null;
        this.luggageId = null;
        this.flight = null;
        this.type = null;
        this.brand = null;
        this.colour = null;
        this.characteristics = null;
        this.language = null;
        this.stage = stage;
        this.otherFiles = new File[0];

        this.luggage = l;
        start();
    }

    @Override
    protected Object call() {
        registrationID = -1;
        if (luggage != null) {
            String query = "SELECT *, lt.lug_type_value FROM `luggage` JOIN luggage_types lt ON lt.lug_type_id = luggage_type WHERE luggage_id = ?;";
            try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, Account.getLoggedInUser().getId());
                ResultSet set = preparedStatement.executeQuery();
                while (set.next()) {
                    firstName = set.getString("first_name");
                    lastName = set.getString("last_name");
                    address = set.getString("address");
                    city = set.getString("city");
                    zip = set.getString("zip");
                    country = set.getString("country");
                    phoneNumber = String.valueOf(set.getInt("phone"));
                    email = set.getString("email");
                    luggageId = set.getString("luggage_tag");
                    flight = set.getString("flight_id");
                    type = set.getString("lug_type_value");
                    brand = set.getString("brand");
                    colour = set.getString("colour");
                    characteristics = set.getString("characteristics");
                    language = set.getString("language");
                }
                registrationID = luggage.getId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String query = "SELECT LAST_INSERT_ID(), CONCAT(ac.first_name, ' ', ac.last_name) AS full_name FROM accounts " +
                "ac WHERE ac.account_id = ?;";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, Account.getLoggedInUser().getId());

            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                registrationID = registrationID != -1 ? registrationID : set.getInt(1);
                employee = set.getString(2);
            }
            promptForPDF();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void promptForPDF() {
        Platform.runLater(() ->
        {
            File file = new PDF("PDF_Registration_Copy_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "_" + UUID
                    .randomUUID().toString(), "Registration Copy", false, stage).createRegistrationCopy(firstName,
                    lastName, address, city, zip, country, phoneNumber, email, luggageId, flight, type, brand,
                    colour, characteristics, language, registrationID, employee).exportPDF();
            try {
                Desktop.getDesktop().open(file);
                for (File f : otherFiles) Desktop.getDesktop().open(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
