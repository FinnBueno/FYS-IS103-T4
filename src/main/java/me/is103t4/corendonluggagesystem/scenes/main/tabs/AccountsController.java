/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import me.is103t4.corendonluggagesystem.scenes.Controller;

/**
 *
 * @author roy
 */
public class AccountsController extends Controller {

    @FXML
    private TableColumn idcolumn;
    @FXML
    private TableColumn namecolumn;
    @FXML
    private TableColumn mailcolumn;
    @FXML
    private TableColumn rolecolumn;
    @FXML
    private TableColumn statuscolumn;

    @FXML
    public void addAccount(ActionEvent event) {

    }

    @FXML
    public void editAccount(ActionEvent event) {

    }

    @FXML
    public void changeFilter(ActionEvent event) {

    }

    @FXML
    public void clearFilter(ActionEvent event) {

    }
}
