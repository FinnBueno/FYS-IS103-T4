/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.scenes.Controller;

/**
 * The controller for the main frame interface
 *
 * @author Finn Bon
 */
public class MainFrameController extends Controller {

    @FXML
    private TabPane tabPane;

    @Override
    public void postInit() {
        Tabs.initAll(main);

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            Tabs tab = Tabs.getTabById(newTab.getId());
            if (tab == null)
                return;
            tab.setRoot(0);
        });
    }

    public void fillTabPane() {
        tabPane.getTabs().
                clear();
        Tabs[] tabs = Tabs.getTabsForRole(Account.getLoggedInUser().
                getRole());
        for (Tabs tab : tabs) {
            tabPane.getTabs().
                    add(tab.getTab());
        }
    }

}
