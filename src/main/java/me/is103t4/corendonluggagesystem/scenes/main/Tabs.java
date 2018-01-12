/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import me.is103t4.corendonluggagesystem.LugSysMain;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TAB SIZE: WIDTH: 900 Height: 465
 *
 * @author Finn Bon
 */
public enum Tabs {

    HOME("Home"),
    LOST_LUGGAGE(AccountRole.EMPLOYEE, "LostLuggage"),
    FOUND_LUGGAGE(AccountRole.EMPLOYEE, "FoundLuggage"),
    DAMAGED_LUGGAGE(AccountRole.EMPLOYEE, "DamagedLuggage"),
    OVERVIEW(AccountRole.EMPLOYEE, "LuggageOverview", "EditLuggage"),
    STATISTICS(new AccountRole[] {AccountRole.ADMIN, AccountRole.MANAGERS }, "Statistics"),
    ACCOUNTS(AccountRole.ADMIN, "Accounts", "NewAccount", "FilterAccount"),
    CONFIGURATIONS("Configurations");

    private Tab tab;
    private final Pane[] root;
    private final Controller[] controller;
    private final String[] fxmlURL;
    private final String name;
    private final List<AccountRole> roles;
    private int activeRoot;

    Tabs(String... names) {
        this(AccountRole.values(), names);
    }

    Tabs(AccountRole r, String... names) {
        this(new AccountRole[] {r}, names);
    }

    Tabs(AccountRole[] r, String... names) {
        roles = Arrays.asList(r);
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
     */
    public void initialize(LugSysMain main) {
        // generate a tab
        System.out.println(name());
        tab = generateTab();

        for (int i = 0; i < fxmlURL.length; i++) {
            String url = fxmlURL[i];
            try {
                FXMLLoader loader = new FXMLLoader(getClass().
                        getResource(url));

                root[i] = loader.load();
                Controller cntrlr = loader.getController();
                if (cntrlr != null) {
                    cntrlr.setMain(main);
                    cntrlr.setScene(main.getScene());
                    cntrlr.init(false);
                    activeRoot = 0;
                }
                controller[i] = cntrlr;

            } catch (IOException ex) {
                Logger.getLogger(Tabs.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
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

    public void setRoot(int i) {
        activeRoot = i;
        tab.setContent(root[i]);
    }

    public Tab getTab() {
        return tab;
    }

    public Tab generateTab() {
        tab = new Tab(splitCamelCase(name));
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
        for (Tabs tab : values())
            if (tab.roles.contains(role))
                list.add(tab);
        return list.toArray(new Tabs[list.size()]);
    }

    public static void initAll(LugSysMain lugSysMain) {
        for (Tabs tab : values()) {
            tab.initialize(lugSysMain);
        }
    }

    public static Tabs getTabById(String id) {
        if (id == null)
            return null;
        for (Tabs tab : values()) {
            if (id.equals(tab.getTab().
                    getId())) {
                return tab;
            }
        }
        return null;
    }

    public boolean isOpen(int rootId) {
        MainFrameController main = ((MainFrameController) Scenes.MAIN.getController());
        return Scenes.MAIN.isOpen() && main.getActiveTab().getId().equals(getTab().getId()) && activeRoot == rootId;
    }

    public int getRootAmount() {
        return root.length;
    }
}
