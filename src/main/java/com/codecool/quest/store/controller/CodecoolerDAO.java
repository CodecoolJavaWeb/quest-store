package com.codecool.quest.store.controller;

import com.codecool.quest.store.model.Codecooler;

import java.util.Set;

public interface CodecoolerDAO {

    Set<Codecooler> getAllCodecoolers();
    Codecooler getCodecoolerById();
    Set<Codecooler> getCodecoolersByClassId(int classId);
    void addCodecooler(Codecooler codecooler);
    void updateCodecooler(Codecooler codecooler);
    void deleteCodecooler(Codecooler codecooler);
}
