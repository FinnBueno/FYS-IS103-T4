/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.account;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import me.is103t4.corendonluggagesystem.scenes.Scenes;

/**
 * @author Finn Bon
 */
public class Account {

    private static Account currentUser;

    private SimpleStringProperty username, email, firstName, lastName, phoneNumber, code;
    private SimpleObjectProperty<AccountRole> role;
    private SimpleBooleanProperty activated;

    public Account(String code, String username, String firstName, String lastName, String phoneNumber, AccountRole role, String email) {
        this.username = new SimpleStringProperty(username);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.code = new SimpleStringProperty(code);
        this.role = new SimpleObjectProperty<>(role);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.activated = new SimpleBooleanProperty(true);
    }

    public boolean isActivated() {
        return activated.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public void login() {
        currentUser = this;
    }

    public String getCode() {
        return code.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }

    public AccountRole getRole() {
        return role.get();
    }

    public static Account getLoggedInUser() {
        return currentUser;
    }

    public void logout() {
        phoneNumber = null;
        firstName = null;
        lastName = null;
        code = null;
        username = null;
        email = null;
        role = null;
        currentUser = null;
        Scenes.LOGIN.setToScene();
    }

}
