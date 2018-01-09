package me.is103t4.corendonluggagesystem.database;

import me.is103t4.corendonluggagesystem.email.IEmail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final String password = "<INSERT PASSWORD>";
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
     * Used to create the table structure in the database
     */
    private void create() throws SQLException {
        // read createTables.sql
        URL urlToFile = getClass().getResource("/sql/createTables.sql");
        // read contents into StringBuilder with BufferedReader
        StringBuilder sb = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(urlToFile.
                openStream()))) {
            String line;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(IEmail.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        if (sb == null) {
            throw new SQLException("Could not create databases! Application will not work properly!");
        }

        // execute query to create all tables if they don't exist already
        try (PreparedStatement preparedStatement = connection.
                prepareStatement(sb.toString().replaceAll("mydb", db))) {

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Starts the connection to the database
     * @throws ClassNotFoundException If the {@link com.mysql.jdbc.Driver} could not be found
     * @throws SQLException If the connection could not be started
     */
    public void open() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        connection = DriverManager.getConnection(url, username, password);
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
