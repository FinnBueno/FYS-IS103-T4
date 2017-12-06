/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;

/**
 * The controller for the main frame interface
 *
 * @author Finn Bon
 */
public class MainFrameController extends Controller {

    @FXML
    private TabPane tabPane;

    @FXML
    private ComboBox<String> langBox;

    @Override
    public boolean isOpen() {
        return Scenes.MAIN.isOpen();
    }

    @Override
    public void postInit() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            Tabs tab = Tabs.getTabById(newTab.getId());
            if (tab == null)
                return;
            tab.setRoot(0);
        });

        // set language combobox
        String notOnLine = "dutch";
        String onLine = "british";
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll(notOnLine, onLine);
        langBox.getItems().clear();
        langBox.getItems().addAll(options);
        langBox.setCellFactory(c -> new StringImageCell());
        langBox.setButtonCell(new StringImageCell());

        Tabs.initAll(main);
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

    @FXML
    public void logout() {
        Account.getLoggedInUser().logout();
    }

    public Tab getActiveTab() {
        return tabPane.getSelectionModel().getSelectedItem();
    }

    // A custom ListCell that displays an image and string
    static class StringImageCell extends ListCell<String> {

        Label label;

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null) {
                setItem(null);
                setGraphic(null);
            } else {
                setText("");
                ImageView image = getImageView(item);
                label = new Label("", image);
                setGraphic(label);
            }
        }

        private ImageView getImageView(String imageName) {
            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/images/" + imageName + "_flag.jpg")));
            image.setFitHeight(20);
            image.setFitWidth(30);
            return image;
        }

    }

}
