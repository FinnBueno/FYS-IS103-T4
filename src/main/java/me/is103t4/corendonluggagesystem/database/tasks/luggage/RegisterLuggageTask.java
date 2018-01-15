package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import javafx.scene.paint.Color;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.dropbox.DropboxHandler;
import me.is103t4.corendonluggagesystem.matching.Luggage;


import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.scene.control.CheckBox;

public class RegisterLuggageTask extends DBTask<Boolean> {

    
    private final String type;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String city;
    private final String zip;
    private final String phone;
    private final String email;
    private final String lang;
    private final String lugType;
    private final String lugTag;
    private final String brand;
    private final Color colour;
    private final String characteristics;
    private final File photo;
    private final String flight_id;
    private final Account employee;

    public RegisterLuggageTask(String type, String firstName, String lastName, String address, String city,
                               String zip, String phone, String email, String lang, String lugType, String
                                       lugTag, String brand, Color
                                       colour, String characteristics, File photo, String flight_id, Account
                                       employee) {
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.lang = getLang(lang);
        this.lugType = lugType;
        this.lugTag = lugTag;
        this.brand = brand;
        this.colour = colour;
        this.characteristics = characteristics;
        this.photo = photo;
        this.flight_id = flight_id.split(" - ")[0];
        this.employee = employee;

        start();
    }

    @Override
    protected Boolean call() {
        String query = "INSERT INTO `luggage` (`register_type`, `first_name`, `last_name`, `address`, " +
                "`city`, `zip`, `phone`, `email`, `language`, `luggage_type`, `luggage_tag`, `brand`, `colour`, " +
                "`characteristics`, `photo`, `flight_id`, `employee`, `date`) VALUES ((SELECT id FROM statusses WHERE value =" +
                " ?), ?, ?, ?, ?, ?, ?, ?, ?, (SELECT lug_type_id FROM luggage_types WHERE lug_type_value = ?), ?, ?," +
                " ?, ?, ?, ?, (SELECT account_id FROM `accounts` WHERE username = ? AND code = ?), ?);";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            DBHandler.PreparingStatement preparingStatement = new DBHandler.PreparingStatement(ps);
            preparingStatement.setString(1, type);
            preparingStatement.setString(2, firstName);
            preparingStatement.setString(3, lastName);
            preparingStatement.setString(4, address);
            preparingStatement.setString(5, city);
            preparingStatement.setString(6, zip);
            preparingStatement.setString(7, phone);
            preparingStatement.setString(8, email);
            preparingStatement.setString(9, lang);
            preparingStatement.setString(10, lugType);
            preparingStatement.setString(11, lugTag);
            preparingStatement.setString(12, brand);
            preparingStatement.setString(13, toHex(colour));
            preparingStatement.setString(14, characteristics);
            preparingStatement.setString(15, uploadPhoto(photo));
            preparingStatement.setString(16, flight_id);
            preparingStatement.setString(17, employee.getUsername());
            preparingStatement.setString(18, employee.getCode());
            preparingStatement.setDate(19, Date.valueOf(LocalDate.now()));

            return ps.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
      private String toHex(Color colour) {
        if (colour == null ) {
            return null;
        }
        return String.format("%02x%02x%02x", (int) (colour.getRed() * 255), (int) (colour.getGreen() * 255), (int)
                (colour.getBlue() * 255));
    }

    private String uploadPhoto(File photo) {
        return DropboxHandler.HANDLER.uploadPhoto(photo);
    }

    private String getLang(String lang) {
        return lang == null || lang.equalsIgnoreCase("english") ? "ENG" : "NL";
    }
}
