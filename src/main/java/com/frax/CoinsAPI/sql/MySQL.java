package com.frax.CoinsAPI.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private String username;
    private String password;
    private String database;
    private int port;
    private String host;

    private Connection con;

    public MySQL(String username, String password, String database, int port, String host) {
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;
        this.host = host;
    }

    public void openCon() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        if (con != null) return true;
        else return false;
    }

    public void createTable() {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Coins(username VARCHAR(16), coins INT(8))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getCon() {
        return con;
    }
}
