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
    public static final String SENDGRIDKEY = "SENDGRIDAPIKEY";
    public static final String DROPBOXKEY = "DROPBOXAPIKEY";
    public static final String LANGUAGE = "LANGUAGE";

    private final static PreferencesManager INSTANCE = new PreferencesManager();
    public static final String DB_CONFIGURED = "DB.configured";
    public static final String DB_USER = "DB.user";
    public static final String DB_PASS = "DB.pass";
    public static final String DB_DB = "DB.db";
    public static final String DB_HOST = "DB.host";
    public static final String DB_PORT = "DB.port";

    public static PreferencesManager get() {
        return INSTANCE;
    }

    private final Preferences prefs;

    private PreferencesManager() {
        prefs = Preferences.userRoot().node("CorendonLuggageDetectiveSystem");
    }

    public void set(String path, String value) {
        prefs.put(path, value);
    }

    public String get(String path) {
        return prefs.get(path, null);
    }
}
