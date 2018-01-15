package me.is103t4.corendonluggagesystem.matching;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class Luggage implements Serializable {

    private StringProperty status = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty tag = new SimpleStringProperty();
    private StringProperty brand = new SimpleStringProperty();
    private StringProperty colour = new SimpleStringProperty();
    private StringProperty characteristics = new SimpleStringProperty();
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty flight = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private StringProperty costs = new SimpleStringProperty();

    private final int id;

    public Luggage(int id, String registerType, String type, String tag, String brand, String colour, String characteristics, String firstName,
                   String lastName, String city, String address, String flight, LocalDate date, int costs) {
        this.id = id;
        this.status.set(registerType);
        this.type.set(type);
        this.tag.set(tag);
        this.brand.set(brand);
        this.colour.set(colour);
        this.characteristics.set(characteristics);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.city.set(city);
        this.address.set(address);
        this.flight.set(flight);
        this.date.set(date);
        this.costs.set(String.valueOf(costs));
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status.get();
    }

    public String getType() {
        return type.get();
    }

    public String getTag() {
        return tag.get();
    }

    public String getBrand() {
        return brand.get();
    }

    public String getCharacteristics() {
        return characteristics.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getColour() {
        return colour.get();
    }

    public String getFlight() {
        return flight.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

}
