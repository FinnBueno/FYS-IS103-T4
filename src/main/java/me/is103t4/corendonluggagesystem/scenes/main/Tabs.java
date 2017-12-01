/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import me.is103t4.corendonluggagesystem.LugSysMain;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.scenes.Controller;

/**
 *
 * TAB SIZE: WIDTH: 900 Height: 465
 *
 * @author Finn Bon
 */
public enum Tabs {

    HOME("Home"),
    LOST_LUGGAGE(AccountRole.EMPLOYEE, "LostLuggage"),
    FOUND_LUGGAGE(AccountRole.EMPLOYEE, "FoundLuggage"),
    DAMAGED_LUGGAGE(AccountRole.EMPLOYEE, "DamagedLuggage"),
    STATISTICS(AccountRole.ADMIN, "Statistics"),
    ACCOUNTS(AccountRole.ADMIN, "Accounts", "NewAccount", "FilterAccount")
    CONFIGURATIONS("Configurations");

    private Tab tab;
    private final Pane[] root;
    private final Controller[] controller;
    private final String[] fxmlURL;
    private final String name;
    private final AccountRole role;

    private Tabs(String... names) {
	this(null, names);
    }

    private Tabs(AccountRole r, String... names) {
	role = r;
	int length = names.length;
	controller = new Controller[length];
	root = new Pane[length];
	fxmlURL = new String[length];
	name = names[0];
	for (int i = 0; i < names.length; i++)
	    fxmlURL[i] = "/fxml/tabs/" + names[i] + "Interface.fxml";
    }

    /**
     * Initialize all tab entries
     *
     * @param main The main app class
     * @return Whether initialization was successful or not
     */
    public boolean initialize(LugSysMain main) {
        System.out.println(name);
	// generate a tab
	tab = generateTab();

	for (int i = 0; i < fxmlURL.length; i++) {
	    String url = fxmlURL[i];
	    try {
		FXMLLoader loader = new FXMLLoader(getClass().
			getResource(url));

		root[i] = loader.load();
		Controller cntrlr = (Controller) loader.getController();
		if (cntrlr != null) {
		    cntrlr.setMain(main);
		    cntrlr.postInit();
		}
		controller[i] = cntrlr;

	    } catch (IOException ex) {
		Logger.getLogger(Tabs.class.getName()).
			log(Level.SEVERE, null, ex);
		return false;
	    }
	}
	return true;
    }

    @Deprecated
    public Controller getController() {
	return controller[0];
    }

    @Deprecated
    public Pane getRoot() {
	return root[0];
    }

    public Controller getController(int i) {
	return controller[i];
    }

    public Pane getRoot(int i) {
	return root[i];
    }

    public void setRoot(int i) {
	tab.setContent(root[i]);
    }

    public Tab getTab() {
	return tab;
    }

    public Tab generateTab() {
	tab  = new Tab(splitCamelCase(name));
	tab.setId(UUID.randomUUID().toString());
	return tab;
    }

    private String splitCamelCase(String s) {
	return s.replaceAll(
		String.format("%s|%s|%s",
			"(?<=[A-Z])(?=[A-Z][a-z])",
			"(?<=[^A-Z])(?=[A-Z])",
			"(?<=[A-Za-z])(?=[^A-Za-z])"
		),
		" "
	);
    }

    public static Tabs[] getTabsForRole(AccountRole role) {
	if (role == AccountRole.DEVELOPER) {
	    return values();
	}

	List<Tabs> list = new ArrayList<>();
	for (Tabs tab : values()) {
	    if (tab.role == role || tab.role == null) {
		list.add(tab);
	    }
	}
	return list.toArray(new Tabs[list.size()]);
    }

    public static void initAll(LugSysMain lugSysMain) {
	for (Tabs tab : values()) {
	    tab.initialize(lugSysMain);
	}
    }

    public static Tabs getTabById(String id) {
	for (Tabs tab : values()) {
	    if (id.equals(tab.getTab().
		    getId())) {
		return tab;
	    }
	}
	return null;
    }

}
