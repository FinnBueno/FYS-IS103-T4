/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.account;

import javafx.beans.property.*;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.scenes.main.tabs.ConfigurationsController;

/**
 * @author Finn Bon
 */
public class Account {

    private static Account currentUser;

    private StringProperty username;
    private StringProperty email;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty phoneNumber;
    private StringProperty code;
    private ObjectProperty<AccountRole> role;
    private boolean activated;
    private int id;

    public Account(int id, String code, String username, String firstName, String lastName, String phoneNumber,
                   AccountRole role, String email, boolean activated) {
        this.id = id;
        this.username = new SimpleStringProperty(username);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.code = new SimpleStringProperty(code);
        this.role = new SimpleObjectProperty<>(role);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
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
        for (Tabs tab : Tabs.values())
            for (int i = 0; i < tab.getRootAmount(); i++) {
                Controller controller = tab.getController(i);
                if (controller != null) controller.postInit();
            }
        ConfigurationsController c = (ConfigurationsController) Tabs.CONFIGURATIONS.getController(0);
        if (c != null)
            c.login();
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

    public int getId() {
        return id;
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
