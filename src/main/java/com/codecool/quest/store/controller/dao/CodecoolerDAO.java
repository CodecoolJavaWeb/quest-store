package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Codecooler;

import java.util.Set;

public interface CodecoolerDAO {

    Set<Codecooler> getAllCodecoolers();
    Codecooler getCodecoolerById(int id);
    Set<Codecooler> getCodecoolersByClassName(String className);
    Set<Codecooler> getCodecoolersBySearchTerm(String searchTerm);
    void addCodecooler(Codecooler codecooler);
    void updateCodecooler(Codecooler codecooler);
    void deleteCodecooler(Codecooler codecooler);
    Codecooler getCodecoolerByBasicDataId(int basicDataId);
}
