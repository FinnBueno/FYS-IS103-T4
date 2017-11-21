/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import me.is103t4.corendonluggagesystem.LugSysMain;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.scenes.Controller;

/**
 * 
 * TAB SIZE:
 *   WIDTH:  900
 *   Height: 465
 * 
 * @author Finn Bon
 */
public enum Tabs {

    LOST_LUGGAGE("LostLuggage", AccountRole.EMPLOYEE),
    FOUND_LUGGAGE("FoundLuggage", AccountRole.EMPLOYEE),
    ACCOUNTS("Accounts", AccountRole.ADMIN);
    
    private Pane root;
    private Controller controller;
    private Tab tab;
    private final String fxmlURL;
    private final String name;
    private final AccountRole role;
    
    private Tabs(String name, AccountRole r) {
	role = r;
	fxmlURL = "/fxml/tabs/" + (this.name = name) + "Interface.fxml";
	System.out.println(fxmlURL);
    }

    public boolean initialize(LugSysMain main) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().
		    getResource(fxmlURL));

	    root = loader.load();
	    controller = (Controller) loader.getController();
	    if (controller != null) {
		controller.setMain(main);
		controller.postInit();
	    }
	    tab = generateTab();
	    return true;
	} catch (IOException ex) {
	    Logger.getLogger(Tabs.class.getName()).
		    log(Level.SEVERE, null, ex);
	    return false;
	}
    }

    public Controller getController() {
	return controller;
    }

    public Pane getRoot() {
	return root;
    }

    public Tab getTab() {
	return tab;
    }
    
    public Tab generateTab() {
	String tabName = splitCamelCase(name);
	Tab realTab = new Tab(tabName);
	realTab.setContent(root);
	return realTab;
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
	if (role == AccountRole.DEVELOPER)
	    return values();
	
	List<Tabs> list = new ArrayList<>();
	for (Tabs tab : values())
	    if (tab.role == role)
		list.add(tab);
    	return list.toArray(new Tabs[list.size()]);
    }

    public static void initAll(LugSysMain lugSysMain) {
	for (Tabs tab : values()) {
	    tab.initialize(lugSysMain);
	}
    }

}
