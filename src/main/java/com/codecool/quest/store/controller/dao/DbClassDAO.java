package com.codecool.quest.store.controller.dao;

import java.sql.*;
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

    @Override
    public void addClass(String className) {
        String sql = "INSERT INTO classes (class_name) VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, className);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteClass(String className) {
        String sql = "DELETE FROM classes WHERE class_name = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, className);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
