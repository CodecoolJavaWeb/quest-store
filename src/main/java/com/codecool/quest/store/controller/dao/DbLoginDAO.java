package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.controller.helpers.AccountType;

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
    public AccountType getAccountTypeById(int basicDataId) {
        if (isIdInTable(basicDataId, "admins")) {
            return AccountType.ADMIN;
        } else if (isIdInTable(basicDataId, "mentors")) {
            return AccountType.MENTOR;
        }
        return AccountType.CODECOOLER;
    }

    private boolean isIdInTable(int basicDataId, String tableName) {
        String sql = "SELECT * FROM "+ tableName +" WHERE basic_data_id = ?";
        boolean result = false;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, basicDataId);
            ResultSet resultSet = statement.executeQuery();
            result = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
