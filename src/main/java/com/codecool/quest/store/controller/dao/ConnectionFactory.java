package com.codecool.quest.store.controller.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    final String URL = "jdbc:postgresql://localhost:5432/queststore";
    final String USER = "creepyguy";
    final String PASS = "magic";

    private static Connection connection = null;

    public ConnectionFactory() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
