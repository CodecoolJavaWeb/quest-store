package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.BasicUserData;
import com.codecool.quest.store.model.Codecooler;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DbCodecoolerDAO implements CodecoolerDAO {

    private Connection connection;
    DAOUtils daoUtils = new DAOUtils();

    public DbCodecoolerDAO(Connection connection) {
        this.connection = connection;
    }

    private Codecooler extractCodecoolerFromResultSet(ResultSet resultSet) throws SQLException {
        Codecooler codecooler = new Codecooler();
        codecooler.setId(resultSet.getInt("id"));
        codecooler.setClassName(resultSet.getString("class_name"));
        codecooler.setExp(resultSet.getInt("exp"));
        codecooler.setBalance(resultSet.getInt("balance"));
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
    public boolean addCodecooler(Codecooler codecooler) {
        String sql = "INSERT INTO codecoolers (first_name, last_name, email, password) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codecooler.getBasicUserData().getFirstName());
            statement.setString(2, codecooler.getBasicUserData().getLastName());
            statement.setString(3, codecooler.getBasicUserData().getEmail());
            statement.setString(4, codecooler.getBasicUserData().getPassword());
            int i = statement.executeUpdate();
            if (i == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCodecooler(Codecooler codecooler) {
//        String sql = "UPDATE codecoolers SET first_name = ?, last_name = ?, email = ?, password = ?, " +
//                "class_id = ?, exp = ?, balance = ?, team_id = ? WHERE id = ?;";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, codecooler.getBasicUserData().getFirstName());
//            statement.setString(2, codecooler.getBasicUserData().getLastName());
//            statement.setString(3, codecooler.getBasicUserData().getEmail());
//            statement.setString(4, codecooler.getBasicUserData().getPassword());
//            String classId = codecooler.getClassName();
//            if (classId == 0) {
//                statement.setNull(5, Types.INTEGER);
//            } else {
//                statement.setInt(5, classId);
//            }
//            statement.setInt(6, codecooler.getExp());
//            statement.setInt(7, codecooler.getBalance());
//            String teamId = codecooler.getTeamName();
//            if (teamId == 0) {
//                statement.setNull(8, Types.INTEGER);
//            } else {
//                statement.setInt(8, teamId);
//            }
//            statement.setInt(9, codecooler.getId());
//            int i = statement.executeUpdate();
//            if (i == 1)
//                return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return false;
    }

    @Override
    public boolean deleteCodecooler(Codecooler codecooler) {
        String sql = "DELETE FROM codecoolers WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codecooler.getId());
            int i = statement.executeUpdate();
            if (i == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
