package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbSessionDAO implements SessionDAO {

    private Connection connection;

    public DbSessionDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addSession(Session session) {
        String sql = "INSERT INTO sessions (session_id, basic_data_id, account_type) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, session.getSessionId());
            statement.setInt(2, session.getBasicDataId());
            statement.setInt(3, session.getAccountType().getValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSessionById(String sessionId) {
        String sql = "DELETE FROM sessions WHERE session_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Session getSession(String sessionId) {
        String sql = "SELECT * FROM sessions WHERE session_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractSessionFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Session extractSessionFromResultSet(ResultSet resultSet) throws SQLException {
        Session session = new Session();
        session.setSessionId(resultSet.getString("session_id"));
        session.setBasicDataId(resultSet.getInt("basic_data_id"));
        session.setAccountType(AccountType.fromInt(resultSet.getInt("account_type")));
        return session;
    }
}
