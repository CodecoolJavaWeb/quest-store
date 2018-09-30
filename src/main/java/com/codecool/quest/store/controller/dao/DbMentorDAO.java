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

    @Override
    public Set<Mentor> getMentorsByClass(String className) {
        String sql = "SELECT m.id, b.first_name, b.last_name, b.email, b.password, c.class_name FROM " +
                "((mentors AS m INNER JOIN basic_user_data AS b ON m.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON m.class_id = c.id) WHERE class_name = ?;";
        Set<Mentor> mentors = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, className);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                mentors.add(extractMentorFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mentors;
    }

    @Override
    public Set<Mentor> getMentorsBySearchTerm(String searchTerm) {
        searchTerm = "%" + searchTerm + "%";
        String sql = "SELECT m.id, b.first_name, b.last_name, b.email, b.password, c.class_name FROM " +
                "((mentors AS m INNER JOIN basic_user_data AS b ON m.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON m.class_id = c.id) " +
                "WHERE first_name LIKE ? " +
                "OR last_name LIKE ? " +
                "OR email LIKE ?;";
        Set<Mentor> mentors = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchTerm);
            statement.setString(2, searchTerm);
            statement.setString(3, searchTerm);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                mentors.add(extractMentorFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mentors;
    }
}
