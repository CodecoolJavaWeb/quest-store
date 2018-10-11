package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;

import java.util.Set;

public interface ArtifactDAO {
    Set<Artifact> getAllArtifacts();
    Artifact getArtifactById(int id);
    Set<Artifact> getBoughtArtifactsByCodecooler(Codecooler codecooler);
    void addArtifactToBought(Artifact artifact, Codecooler codecooler);

    void removeUsedArtifactByCodecooler(Artifact artifact, Codecooler codecooler);

    void updateArtifact(Artifact artifact);

    void addArtifact(Artifact artifact);
}
