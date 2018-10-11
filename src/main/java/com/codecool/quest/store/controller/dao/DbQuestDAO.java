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
        quest.setDescription(resultSet.getString("description"));
        quest.setValue(resultSet.getInt("reward"));
        quest.setExtra(resultSet.getBoolean("is_extra"));
        quest.setImagePath(resultSet.getString("img_path"));

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

    @Override
    public Set<Quest> getDoneQuestsByCodecooler(Codecooler codecooler){
        String sql = "SELECT q.* FROM done_quests AS dq INNER JOIN quests AS q ON dq.quest_id = q.id " +
                "WHERE dq.codecooler_id = ?;";
        Set<Quest> quests = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codecooler.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                quests.add(extractQuestFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quests;
    }

    @Override
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
        String sql = "INSERT INTO quests (quest_name, description, reward, is_extra, img_path) " +
                "VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, quest.getName());
            statement.setString(2, quest.getDescription());
            statement.setInt(3, quest.getValue());
            statement.setBoolean(4, quest.isExtra());
            statement.setString(5, quest.getImagePath());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateQuest(Quest quest) {
        String sql = "UPDATE quests SET quest_name = ?, description = ?, reward = ?, is_extra = ?, img_path = ? " +
                "WHERE id = ? ;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, quest.getName());
            statement.setString(2, quest.getDescription());
            statement.setInt(3, quest.getValue());
            statement.setBoolean(4, quest.isExtra());
            statement.setString(5, quest.getImagePath());
            statement.setInt(6, quest.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Quest getQuestById(int questId) {
        String sql = "SELECT * FROM quests WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractQuestFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCountOfDoneQuestByCodecooler(Quest quest, Codecooler codecooler) {
        String sql = "SELECT COUNT(id) FROM done_quests WHERE quest_id = ? AND codecooler_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quest.getId());
            statement.setInt(2, codecooler.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
