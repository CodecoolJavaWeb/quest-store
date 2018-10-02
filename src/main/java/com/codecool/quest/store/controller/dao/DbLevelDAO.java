package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Level;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DbLevelDAO implements LevelDAO {

    Connection connection;

    public DbLevelDAO(Connection connection) {
        this.connection = connection;
    }

    private Level extractLevelFromResultSet(ResultSet resultSet) throws SQLException {
        Level level = new Level();
        level.setId(resultSet.getInt("id"));
        level.setName(resultSet.getString("level_name"));
        level.setStartValue(resultSet.getInt("start_value"));
        level.setEndValue(resultSet.getInt("end_value"));
        return level;
    }

    @Override
    public Set<Level> getAllLevels() {
        String sql = "SELECT * FROM exp_levels;";
        Set<Level> levels = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                levels.add(extractLevelFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return levels;
    }

    @Override
    public void updateLevel(Level level) {
        String sql = "UPDATE exp_levels SET level_name = ?, start_value = ?, end_value = ? WHERE id =?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, level.getName());
            statement.setInt(2, level.getStartValue());
            statement.setInt(3, level.getEndValue());
            statement.setInt(4, level.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addLevel(Level level) {
        String sql = "INSERT INTO exp_levels (level_name, start_value, end_value) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, level.getName());
            statement.setInt(2, level.getStartValue());
            statement.setInt(3, level.getEndValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLevel(int levelId) {
        String sql = "DELETE FROM exp_levels WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, levelId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
