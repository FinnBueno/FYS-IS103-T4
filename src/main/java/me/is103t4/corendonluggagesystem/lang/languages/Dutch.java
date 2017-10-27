/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.lang.languages;

import me.is103t4.corendonluggagesystem.lang.Language;

/**
 *
 * @author finnb
 */
public class Dutch extends Language {

    @Override
    public String login() {
	return "Login";
    }

    @Override
    public String username() {
	return "Gebruikersnaam";
    }

    @Override
    public String password() {
	return "Wachtwoord";
    }

}
