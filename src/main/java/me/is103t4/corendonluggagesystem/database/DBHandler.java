package me.is103t4.corendonluggagesystem.database;

import me.is103t4.corendonluggagesystem.email.IEmail;
import me.is103t4.corendonluggagesystem.util.PreferencesManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * This class handles any interaction with the database. Any database
 * connections should be handled via this class ONLY
 *
 * @author Finn Bon
 */
public class DBHandler {

    public static final DBHandler INSTANCE = new DBHandler();

    private final String host = "54.37.228.40";
    private final int port = 3306;
    private final String username = "corendon";
    private final String password = "Corendon1";
    private final String db = "corendon";

    private Connection connection;
    static final Logger LOGGER = Logger.getLogger(DBHandler.class.
            getName());

    public void start() {
        try {
            open();
            // create();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Starts the connection to the database
     * @throws ClassNotFoundException If the {@link com.mysql.jdbc.Driver} could not be found
     * @throws SQLException If the connection could not be started
     */
    public void open() throws ClassNotFoundException, SQLException {

        String configured = PreferencesManager.get().get(PreferencesManager.DB_CONFIGURED);
        String user, pass, port, host, db;
        if (configured == null || !configured.equalsIgnoreCase("true")) {
            user = "corendon";
            pass = "Corendon1";
            db = "corendon";
            host = "54.37.228.40";
            port = "3306";
        } else {
            user = PreferencesManager.get().get(PreferencesManager.DB_USER);
            pass = PreferencesManager.get().get(PreferencesManager.DB_PASS);
            db = PreferencesManager.get().get(PreferencesManager.DB_DB);
            host = PreferencesManager.get().get(PreferencesManager.DB_HOST);
            port = PreferencesManager.get().get(PreferencesManager.DB_PORT);
        }
        System.out.println(configured);
        System.out.println(user);
        System.out.println(pass);
        System.out.println(db);
        System.out.println(host);
        System.out.println(port);

        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        connection = DriverManager.getConnection(url, user, pass);
    }

    /**
     * Close the connection to the database
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Wrapper class used to set the parameters used in {@link PreparedStatement}
     */
    public static class PreparingStatement {

        private PreparedStatement ps;

        public PreparingStatement(PreparedStatement ps) {
            this.ps = ps;
        }

        public PreparingStatement setString(int i, String msg) {
            try {
                ps.setString(i, msg);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return this;
        }

        public PreparingStatement setDate(int i, Date date) {
            try {
                ps.setDate(i, date);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return this;
        }

        public PreparingStatement setFloat(int i, float f) {
            try {
                ps.setFloat(i, f);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return this;
        }

        public PreparingStatement setInt(int i, int j) {
            try {
                ps.setInt(i, j);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return this;
        }

        public PreparingStatement setLong(int i, long l) {
            try {
                ps.setLong(i, l);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return this;
        }

        public PreparingStatement setBytes(int i , byte[] b) {
            try {
                ps.setBytes(i, b);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return this;
        }

    }

}
