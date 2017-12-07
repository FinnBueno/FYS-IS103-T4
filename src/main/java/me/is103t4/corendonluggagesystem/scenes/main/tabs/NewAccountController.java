/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

/**
 * 
 * @author Roy Klein
 */
public class NewAccountController extends Controller {

    @Override
    public boolean isOpen() {
        return Tabs.ACCOUNTS.isOpen(1);
    }
    
}
