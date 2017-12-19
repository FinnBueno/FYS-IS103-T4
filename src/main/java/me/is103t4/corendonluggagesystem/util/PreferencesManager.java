/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.util;

import java.util.prefs.Preferences;

/**
 *
 * @author Sebastiaan Wezenberg
 */
public class PreferencesManager {   
    public static final String SENDGRIDKEY = "SENDGRID_API_KEY";
       
    private final static PreferencesManager INSTANCE = new PreferencesManager();

    public static PreferencesManager get() {
        return INSTANCE;
    }

    private final Preferences prefs;

    private PreferencesManager() {
        prefs = Preferences.userNodeForPackage(getClass());
    }

    public void set(String path, String value) {
        prefs.put(path, value);
    }

    public String get(String path) {
        return prefs.get(path, null);
    }
}
