/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.account;

/**
 *
 * @author finnb
 */
public class Account {

    private static Account currentUser;

    private String name, email;
    private int code;
    private AccountRole role;

    public Account(int code, String name, AccountRole role, String email) {
	this.name = name;
	this.code = code;
	this.role = role;
	this.email = email;
	currentUser = this;
    }

    public int getCode() {
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
