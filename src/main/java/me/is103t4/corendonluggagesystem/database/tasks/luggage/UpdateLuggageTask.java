package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateLuggageTask extends DBTask<Boolean> {

    private final String firstName, lastName, address, city, zip, phone, email, language, luggageTag,
            brand, colour, characteristics, flight_id, status;
    private final int luggageType;

    // the entry's id to update
    private final int id;

    public UpdateLuggageTask(String firstName, String lastName, String address, String city, String zip, String
            phone, String email, String language, int luggageType, String luggageTag, String brand, String colour,
                             String characteristics, String flight_id, String status, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.language = language;
        this.luggageType = luggageType + 1;
        this.luggageTag = luggageTag;
        this.brand = brand;
        this.colour = colour;
        this.characteristics = characteristics;
        this.flight_id = flight_id;
        this.status = status;
        this.id = id;

        start();
    }

    @Override
    protected Boolean call() {
        String query = "UPDATE `luggage` " +
                "SET first_name = ?, " +
                "last_name = ?, " +
                "address = ?, " +
                "city = ?, " +
                "zip = ?, " +
                "phone = ?, " +
                "email = ?, " +
                "language = ?, " +
                "luggage_type = ?, " +
                "luggage_tag = ?, " +
                "brand = ?, " +
                "colour = ?, " +
                "characteristics = ?, " +
                // "photo = ?, " +
                "flight_id = ?, " +
                "register_type = (SELECT id FROM statusses WHERE value = ?) " +
                "WHERE luggage_id = ?;";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, zip);
            preparedStatement.setLong(6, Long.parseLong(phone));
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, getLanguage());
            preparedStatement.setInt(9, luggageType);
            preparedStatement.setString(10, luggageTag);
            preparedStatement.setString(11, brand);
            preparedStatement.setString(12, colour);
            preparedStatement.setString(13, characteristics);
            // preparedStatement.setString(14, getPhoto());
            preparedStatement.setString(14, getFlightId());
            preparedStatement.setString(15, status);
            preparedStatement.setInt(16, id);

            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getLanguage() {
        return language.equalsIgnoreCase("English") ? "ENG" : "NL";
    }

    @SuppressWarnings("unused")
    private String getPhoto() {
        // TODO: Byte array stuff
        return null;
    }

    private String getFlightId() {
        return (flight_id == null || flight_id.length() == 0) ? null : flight_id.split(" - ")[0];
    }
}
