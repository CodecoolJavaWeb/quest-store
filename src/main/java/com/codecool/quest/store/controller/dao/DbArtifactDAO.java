package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DbArtifactDAO implements ArtifactDAO {

    private Connection connection;

    public DbArtifactDAO(Connection connection) {
        this.connection = connection;
    }

    private Artifact extractArtifactFromResultSet(ResultSet resultSet) throws SQLException {
        Artifact artifact = new Artifact();
        artifact.setId(resultSet.getInt("id"));
        artifact.setName(resultSet.getString("artifact_name"));
        artifact.setDescription(resultSet.getString("description"));
        artifact.setPrice(resultSet.getInt("price"));
        artifact.setMagic(resultSet.getBoolean("is_magic"));

        return artifact;
    }

    @Override
    public Set<Artifact> getAllArtifacts() {
        String sql = "SELECT * FROM artifacts;";
        Set<Artifact> artifacts = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                artifacts.add(extractArtifactFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artifacts;
    }

    @Override
    public Artifact getArtifactById(int id) {
        String sql = "SELECT * FROM artifacts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractArtifactFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Set<Artifact> getBoughtArtifactsByCodecooler(Codecooler codecooler) {
        String sql = "SELECT a.* FROM bought_artifacts AS ba INNER JOIN artifacts AS a ON ba.artifact_id = a.id " +
                "WHERE ba.codecooler_id = ?;";
        Set<Artifact> artifacts = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codecooler.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                artifacts.add(extractArtifactFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artifacts;
    }

    @Override
    public void addArtifactToBought(Artifact artifact, Codecooler codecooler) {
        String sql = "INSERT INTO bought_artifacts (artifact_id, codecooler_id) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, artifact.getId());
            statement.setInt(2, codecooler.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removeUsedArtifactByCodecooler(Artifact artifact, Codecooler codecooler) {
        String sql = "DELETE FROM bought_artifacts WHERE ctid=(SELECT ctid FROM bought_artifacts WHERE (artifact_id = ? AND codecooler_id = ?) LIMIT 1);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, artifact.getId());
            statement.setInt(2, codecooler.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
