package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.BasicUserData;
import com.codecool.quest.store.model.Codecooler;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DbCodecoolerDAO implements CodecoolerDAO {

    private Connection connection;
    private DAOUtils daoUtils = new DAOUtils();

    public DbCodecoolerDAO(Connection connection) {
        this.connection = connection;
    }

    private Codecooler extractCodecoolerFromResultSet(ResultSet resultSet) throws SQLException {
        Codecooler codecooler = new Codecooler();
        codecooler.setId(resultSet.getInt("id"));
        codecooler.setExp(resultSet.getInt("exp"));
        codecooler.setBalance(resultSet.getInt("balance"));
        codecooler.setClassName(resultSet.getString("class_name"));
        codecooler.setTeamName(resultSet.getString("team_name"));
        BasicUserData basicUserData = daoUtils.extractBasicUserDataFromResultSet(resultSet);
        codecooler.setBasicUserData(basicUserData);
        return codecooler;
    }

    @Override
    public Set<Codecooler> getAllCodecoolers() {
        String sql = "SELECT s.id, b.first_name, b.last_name, b.email, b.password, c.class_name, s.exp, " +
                "s.balance, t.team_name FROM " +
                "(((codecoolers AS s INNER JOIN basic_user_data AS b ON s.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON s.class_id = c.id) " +
                "LEFT JOIN teams AS t ON s.team_id = t.id);";
        Set<Codecooler> codecoolers = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                codecoolers.add(extractCodecoolerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codecoolers;
    }

    @Override
    public Codecooler getCodecoolerById(int id) {
        String sql = "SELECT s.id, b.first_name, b.last_name, b.email, b.password, c.class_name, s.exp, " +
                "s.balance, t.team_name FROM " +
                "(((codecoolers AS s INNER JOIN basic_user_data AS b ON s.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON s.class_id = c.id) " +
                "LEFT JOIN teams AS t ON s.team_id = t.id) WHERE s.id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractCodecoolerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Codecooler> getCodecoolersByClassName(String className) {
        String sql = "SELECT s.id, b.first_name, b.last_name, b.email, b.password, c.class_name, s.exp, " +
                "s.balance, t.team_name FROM " +
                "(((codecoolers AS s INNER JOIN basic_user_data AS b ON s.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON s.class_id = c.id) " +
                "LEFT JOIN teams AS t ON s.team_id = t.id) WHERE class_name = ?;";
        Set<Codecooler> codecoolers = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, className);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                codecoolers.add(extractCodecoolerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codecoolers;
    }

    @Override
    public void addCodecooler(Codecooler codecooler) {
        String sql = "INSERT INTO basic_user_data (first_name, last_name, email, password) " +
                "VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codecooler.getBasicUserData().getFirstName());
            statement.setString(2, codecooler.getBasicUserData().getLastName());
            statement.setString(3, codecooler.getBasicUserData().getEmail());
            statement.setString(4, codecooler.getBasicUserData().getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "INSERT INTO codecoolers (basic_data_id, class_id) VALUES" +
                "(" +
                "(SELECT id FROM basic_user_data WHERE email = ?)," +
                "(SELECT id FROM classes WHERE class_name = ?)" +
                ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codecooler.getBasicUserData().getEmail());
            statement.setString(2, codecooler.getClassName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateCodecooler(Codecooler codecooler) {
        String sql = "UPDATE codecoolers SET class_id = (SELECT id FROM classes WHERE class_name = ?) WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codecooler.getClassName());
            statement.setInt(2, codecooler.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "UPDATE basic_user_data SET first_name = ?, last_name = ?, email = ?, password = ?" +
                "WHERE id = (SELECT basic_data_id FROM codecoolers WHERE id = ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codecooler.getBasicUserData().getFirstName());
            statement.setString(2, codecooler.getBasicUserData().getLastName());
            statement.setString(3, codecooler.getBasicUserData().getEmail());
            statement.setString(4, codecooler.getBasicUserData().getPassword());
            statement.setInt(5, codecooler.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String teamName = codecooler.getTeamName();
        if (!teamName.equals("")) {
            sql = "UPDATE codecoolers SET team_id = (SELECT id FROM teams WHERE team_name = ?) WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, teamName);
                statement.setInt(2, codecooler.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteCodecooler(Codecooler codecooler) {
        String sql = "DELETE FROM codecoolers WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codecooler.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Codecooler getCodecoolerByBasicDataId(int basicDataId) {
        String sql = "SELECT s.id, b.first_name, b.last_name, b.email, b.password, c.class_name, s.exp, " +
                "s.balance, t.team_name FROM " +
                "(((codecoolers AS s INNER JOIN basic_user_data AS b ON s.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON s.class_id = c.id) " +
                "LEFT JOIN teams AS t ON s.team_id = t.id) WHERE b.id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, basicDataId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractCodecoolerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Codecooler> getCodecoolersBySearchTerm(String searchTerm) {
        searchTerm = "%" + searchTerm + "%";
        String sql = "SELECT cc.id, b.first_name, b.last_name, b.email, b.password, c.class_name, cc.exp, cc.balance, t.team_name FROM " +
                "(((codecoolers AS cc INNER JOIN basic_user_data AS b ON cc.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON cc.class_id = c.id) LEFT JOIN teams AS t ON cc.team_id = t.id) " +
                "WHERE first_name LIKE ? " +
                "OR last_name LIKE ? " +
                "OR email LIKE ?;";
        Set<Codecooler> codecoolers = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchTerm);
            statement.setString(2, searchTerm);
            statement.setString(3, searchTerm);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                codecoolers.add(extractCodecoolerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codecoolers;

    }
}
