package com.codecool.quest.store.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbLoginDAO implements LoginDAO {

    private Connection connection;

    public DbLoginDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getPasswordByEmail(String email) {
        String sql = "SELECT password FROM basic_user_data WHERE email = ?";
        String password = "";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                password = resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    @Override
    public int getIdByEmail(String email) {
        String sql = "SELECT id FROM basic_user_data WHERE email = ?";
        int id = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public String getAccountTypeById(int basicDataId) {
        return "admin";
    }
}
