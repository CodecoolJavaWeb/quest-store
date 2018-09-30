package com.codecool.quest.store.controller.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbClassDAO implements ClassDAO {

    Connection connection;

    public DbClassDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<String> getClassesNames() {
        String sql = "SELECT class_name FROM classes;";
        List<String> classes = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                classes.add(resultSet.getString("class_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }
}
