package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Team;

import java.util.Set;

public interface TeamDAO {

    Set<Team> getTeamsByClassName(String className);
    void addTeam(Team team);
}
