package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;

import java.util.Set;

public interface ArtifactDAO {
    Set<Artifact> getBoughtArtifactsByCodecooler(Codecooler codecooler);
}
