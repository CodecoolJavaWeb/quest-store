package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.model.Quest;

import java.util.List;
import java.util.Set;


public interface QuestDAO {
    List<String> getQuestsNames();
    Set<Quest> getAllQuests();
    Set<Quest> getDoneQuestsByCodecooler(Codecooler codecooler);
    void addDoneQuestByCodecooler(Quest quest, Codecooler codecooler);
    void addQuest(Quest quest);
    void updateQuest(Quest quest);
    Quest getQuestById(int questId);
}
