package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DbTeamDAO implements TeamDAO {

    Connection connection;

    public DbTeamDAO(Connection connection) {
        this.connection = connection;
    }

    private Team extractTeamFromResultSet (ResultSet resultSet) throws SQLException {
        Team team = new Team();
        team.setId(resultSet.getInt("id"));
        team.setName(resultSet.getString("team_name"));
        team.setClassName(resultSet.getString("class_name"));
        return team;
    }

    @Override
    public Set<Team> getTeamsByClassName(String className) {
        String sql = "SELECT t.id, t.team_name, c.class_name FROM teams AS t INNER JOIN classes AS c ON t.class_id = c.id " +
                "WHERE c.class_name = ?;";
        Set<Team> teams = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, className);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                teams.add(extractTeamFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    @Override
    public void addTeam(Team team) {
        String sql = "INSERT INTO teams (team_name, class_id) VALUES (?, (SELECT id FROM classes WHERE class_name = ?));";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, team.getName());
            statement.setString(2, team.getClassName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
