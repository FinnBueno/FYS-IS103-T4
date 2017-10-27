/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.lang;

import me.is103t4.corendonluggagesystem.lang.languages.Dutch;

/**
 *
 * @author Finn Bon
 */
public class Lang extends Language {

    private static Language lang;

    public Lang() {
	this.lang = new Dutch(); // add selection option
    }

    public static Language get() {
	return lang;
    }

    @Override
    public String login() {
	return lang.login();
    }

    @Override
    public String username() {
	return lang.username();
    }

    @Override
    public String password() {
	return lang.password();
    }

}
