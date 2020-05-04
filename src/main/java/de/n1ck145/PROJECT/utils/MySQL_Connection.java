package de.n1ck145.PROJECT.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL_Connection {
    public String host;
    public String port;
    public String database;
    public String username;
    public String password;
    private Connection con;

    public MySQL_Connection(){}
    public MySQL_Connection(String host, String port, String database, String username, String password){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        if (!isConnected())
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("MySQL connencted as " + username + "@" + host + ":" + port);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("MySQL connection error!");
            }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("MySQL connection closed!");        	}
        }
    }

    public boolean isConnected() {
        return !(con == null);
    }

    public void update(String querry) {
        try {
            PreparedStatement ps = con.prepareStatement(querry);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult(String querry) {
        try {
            PreparedStatement ps = con.prepareStatement(querry);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}