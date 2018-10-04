package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Level;

import java.util.Set;

public interface LevelDAO {
    Set<Level> getAllLevels();
    void updateLevel(Level level);
    void addLevel(Level level);
    void deleteLevel(int levelId);
}
