/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.account;

/**
 * @author finnb
 */
public class Account {

    private static Account currentUser;

    private String name, email, firstName, lastName, phoneNumber, code;
    private AccountRole role;

    public Account(String code, String username, String firstName, String lastName, String phoneNumber, AccountRole role, String email) {
        this.name = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void login() {
        currentUser = this;
    }

    public String getCode() {
        return code;
    }

    public String getUsername() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public AccountRole getRole() {
        return role;
    }

    public static Account getLoggedInUser() {
        return currentUser;
    }

}
