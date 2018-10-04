package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.model.Quest;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbQuestDAO implements QuestDAO {

    private Connection connection;

    public DbQuestDAO(Connection connection) {
        this.connection = connection;
    }

    private Quest extractQuestFromResultSet(ResultSet resultSet) throws SQLException {
        Quest quest = new Quest();
        quest.setId(resultSet.getInt("id"));
        quest.setName(resultSet.getString("quest_name"));
        quest.setDescription(resultSet.getString("class_name"));
        quest.setValue(resultSet.getInt("reward"));
        quest.setExtra(resultSet.getBoolean("is_extra"));

        return quest;
    }


    @Override
    public Set<Quest> getAllQuests() {
         String sql = "SELECT * FROM quests;";
        Set<Quest> quests = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                quests.add(extractQuestFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quests;
    }


    @Override
    public List<String> getQuestsNames() {
        String sql = "SELECT quest_name FROM quests;";
        List<String> quests = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                quests.add(resultSet.getString("quest_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quests;
    }

    public List<String> getDoneQuestsByCodecooler(Codecooler codecooler){
        String sql = "SELECT quests.quest_name FROM done_quests INNER JOIN quests ON done_quests.quest_id = quests.id WHERE done_quests.codecooler_id = ?;";
        List<String> quests = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codecooler.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                quests.add(resultSet.getString("quest_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quests;
    }


    public void addDoneQuestByCodecooler(Quest quest, Codecooler codecooler) {
        String sql = "INSERT INTO done_quests (quest_id, codecooler_id) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quest.getId());
            statement.setInt(2, codecooler.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addQuest(Quest quest) {
        String sql = "INSERT INTO quests (quest_name, description, reward, is_extra) " +
                "VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, quest.getName());
            statement.setString(2, quest.getDescription());
            statement.setInt(3, quest.getValue());
            statement.setBoolean(4, quest.isExtra());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateQuest(Quest quest) {
        String sql = "UPDATE quests SET quest_name = ?, description = ?, reward = ?, is_extra = ? WHERE id = ? ;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, quest.getName());
            statement.setString(2, quest.getDescription());
            statement.setInt(3, quest.getValue());
            statement.setBoolean(4, quest.isExtra());
            statement.setInt(5, quest.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
