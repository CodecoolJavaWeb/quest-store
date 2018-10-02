package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.BasicUserData;
import com.codecool.quest.store.model.Mentor;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DbMentorDAO implements MentorDAO {

    Connection connection;
    DAOUtils daoUtils = new DAOUtils();

    public DbMentorDAO(Connection connection) {
        this.connection = connection;
    }

    private Mentor extractMentorFromResultSet(ResultSet resultSet) throws SQLException {
        Mentor mentor = new Mentor();
        mentor.setId(resultSet.getInt("id"));
        mentor.setClassName(resultSet.getString("class_name"));
        BasicUserData basicUserData = daoUtils.extractBasicUserDataFromResultSet(resultSet);
        mentor.setBasicUserData(basicUserData);
        return mentor;
    }



    @Override
    public Set<Mentor> getAllMentors() {
        String sql = "SELECT m.id, b.first_name, b.last_name, b.email, b.password, c.class_name FROM " +
                "((mentors AS m INNER JOIN basic_user_data AS b ON m.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON m.class_id = c.id);";
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

    @Override
    public Mentor getMentorById(int id) {
        String sql = "SELECT m.id, b.first_name, b.last_name, b.email, b.password, c.class_name FROM " +
                "((mentors AS m INNER JOIN basic_user_data AS b ON m.basic_data_id = b.id) " +
                "INNER JOIN classes AS c ON m.class_id = c.id) WHERE m.id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractMentorFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateMentor(Mentor mentor) {
        String sql = "UPDATE mentors SET class_id = (SELECT id FROM classes WHERE class_name = ?) WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mentor.getClassName());
            statement.setInt(2, mentor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "UPDATE basic_user_data SET first_name = ?, last_name = ?, email = ?, password = ?" +
                "WHERE id = (SELECT basic_data_id FROM mentors WHERE id = ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mentor.getBasicUserData().getFirstName());
            statement.setString(2, mentor.getBasicUserData().getLastName());
            statement.setString(3, mentor.getBasicUserData().getEmail());
            statement.setString(4, mentor.getBasicUserData().getPassword());
            statement.setInt(5, mentor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addMentor(Mentor mentor) {
        String sql = "INSERT INTO basic_user_data (first_name, last_name, email, password) " +
                "VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mentor.getBasicUserData().getFirstName());
            statement.setString(2, mentor.getBasicUserData().getLastName());
            statement.setString(3, mentor.getBasicUserData().getEmail());
            statement.setString(4, mentor.getBasicUserData().getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "INSERT INTO mentors (basic_data_id, class_id) VALUES" +
                "(" +
                "(SELECT id FROM basic_user_data WHERE email = ?)," +
                "(SELECT id FROM classes WHERE class_name = ?)" +
                ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mentor.getBasicUserData().getEmail());
            statement.setString(2, mentor.getClassName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
