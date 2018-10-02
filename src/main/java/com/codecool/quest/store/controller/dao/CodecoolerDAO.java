package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Codecooler;

import java.util.Set;

public interface CodecoolerDAO {

    Set<Codecooler> getAllCodecoolers();
    Codecooler getCodecoolerById(int id);
    Set<Codecooler> getCodecoolersByClassName(String className);
    boolean addCodecooler(Codecooler codecooler);
    boolean updateCodecooler(Codecooler codecooler);
    boolean deleteCodecooler(Codecooler codecooler);
}
