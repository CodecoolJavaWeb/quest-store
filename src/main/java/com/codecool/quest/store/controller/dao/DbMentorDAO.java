package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.BasicUserData;
import com.codecool.quest.store.model.Mentor;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DbMentorDAO implements MentorDAO {

    Connection connection;

    public DbMentorDAO(Connection connection) {
        this.connection = connection;
    }

    private Mentor extractMentorFromResultSet(ResultSet resultSet) throws SQLException {
        int basicDataId = resultSet.getInt("basic_data_id");
        BasicUserData basicUserData = getBasicUserData(basicDataId);

        Mentor mentor = new Mentor();
        mentor.setId(resultSet.getInt("id"));
        mentor.setClassId(resultSet.getInt("class_id"));
        mentor.setBasicUserData(basicUserData);
        return mentor;
    }

    private BasicUserData getBasicUserData(int basicDataId) {
        String sql = "SELECT * FROM basic_user_data WHERE id = ?";
        BasicUserData basicUserData = new BasicUserData();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, basicDataId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                basicUserData.setFirstName(resultSet.getString("first_name"));
                basicUserData.setLastName(resultSet.getString("last_name"));
                basicUserData.setEmail(resultSet.getString("email"));
                basicUserData.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return basicUserData;
    }

    @Override
    public Set<Mentor> getAllMentors() {
        String sql = "SELECT * FROM mentors;";
        Set<Mentor> mentors = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                mentors.add(extractMentorFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mentors;
    }
}
