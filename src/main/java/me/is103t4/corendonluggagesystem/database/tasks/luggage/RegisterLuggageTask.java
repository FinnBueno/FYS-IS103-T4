package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import javafx.scene.paint.Color;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.database.LuggageType;
import me.is103t4.corendonluggagesystem.database.RegisterType;

import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class RegisterLuggageTask extends DBTask<Boolean> {

    private final RegisterType type;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String city;
    private final String zip;
    private final String phone;
    private final String email;
    private final String lang;
    private final LuggageType lugType;
    private final String lugTag;
    private final String brand;
    private final Color colour;
    private final String characteristics;
    private final File photo;
    private final String flight_id;
    private final Account employee;
    private final LocalDate date;

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "my_cloud_name",
            "api_key", "my_api_key",
            "api_secret", "my_api_secret"));

    public RegisterLuggageTask(RegisterType type, String firstName, String lastName, String address, String city,
                               String zip, String phone, String email, String lang, LuggageType lugType, String lugTag, String brand, Color
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
        this.flight_id = flight_id;
        this.employee = employee;
        this.date = LocalDate.now();

        start();
    }

    @Override
    protected Boolean call() {
        String query = "INSERT INTO `luggage` (`register_type`, `first_name`, `last_name`, `address`, " +
                "`city`, `zip`, `phone`, `email`, `language`, `luggage_type`, `luggage_tag`, `brand`, `colour`, " +
                "`characteristics`, `photo`, `flight_id`, `employee`, `date`) VALUES (?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT account_id FROM `accounts` WHERE username = ? AND code = ?), ?);";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            DBHandler.PreparingStatement preparingStatement = new DBHandler.PreparingStatement(ps);
            preparingStatement.setInt(1, type.getId());
            preparingStatement.setString(2, firstName);
            preparingStatement.setString(3, lastName);
            preparingStatement.setString(4, address);
            preparingStatement.setString(5, city);
            preparingStatement.setString(6, zip);
            preparingStatement.setString(7, phone);
            preparingStatement.setString(8, email);
            preparingStatement.setString(9, lang);
            preparingStatement.setInt(10, lugType.getId());
            preparingStatement.setString(11, lugTag);
            preparingStatement.setString(12, brand);
            preparingStatement.setString(13, toHex(colour));
            preparingStatement.setString(14, characteristics);
            preparingStatement.setString(15, uploadPhoto(photo));
            preparingStatement.setString(16, flight_id);
            preparingStatement.setString(17, employee.getUsername());
            preparingStatement.setString(18, employee.getCode());
            preparingStatement.setDate(19, Date.valueOf(date));

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String toHex(Color colour) {
        return String.format("%02x%02x%02x", (int) (colour.getRed() * 255), (int) (colour.getGreen() * 255), (int) (colour.getBlue() * 255));
    }

    private String uploadPhoto(File photo) {
        return "NOT YET IMPLEMENTED";
        /*
        try {
            Map map = cloudinary.uploader().upload(photo, ObjectUtils.emptyMap());
            return (String) map.get("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    private String getLang(String lang) {
        return lang.equalsIgnoreCase("english") ? "en" : "nl";
    }
}
