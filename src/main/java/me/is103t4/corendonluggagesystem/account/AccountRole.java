/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.account;

import java.util.ArrayList;
import java.util.List;

/**
 * @author finnb
 */
public enum AccountRole {

    ADMIN(0), MANAGERS(1), EMPLOYEE(2), DEVELOPER(3);

    private final int id;

    AccountRole(int i) {
        id = i;
    }

    public static List<String> getNames() {
        List<String> strings = new ArrayList<>();
        for (AccountRole role : values())
            strings.add(role.toString());
        return strings;
    }

    public int getId() {
        return id;
    }

    public static AccountRole fromId(int id) {
        for (AccountRole role : values())
            if (role.id == id)
                return role;
        return null;
    }

    public String toString() {
        return name().toCharArray()[0] + name().toLowerCase().substring(1);
    }

}
