/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.account;

/**
 *
 * @author finnb
 */
public enum AccountRole {
    
    ADMIN(0), MANAGERS(1), EMPLOYEE(2), DEVELOPER(3);
    
    private final int id;
    
    private AccountRole(int i) {
	id = i;
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
    
}
