/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.database;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.Task;

/**
 * Abstract class for database task classes to extend to implement some basic
 * functionality
 *
 * @author Finn Bon
 */
public abstract class DBTask<T> extends Task {

    protected final Connection conn;
    protected final Logger logger;

    public DBTask() {
        this.logger = DBHandler.LOGGER;
        this.conn = DBHandler.INSTANCE.getConnection();
        setOnFailed(t -> {
            logger.log(Level.SEVERE, null, getException());
        });
    }

    protected void start() {
        Thread th = new Thread(this);
        th.setDaemon(true);
        th.start();
    }

}
