package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Mentor;

import java.util.Set;

public interface MentorDAO {
    Set<Mentor> getAllMentors();
    Set<Mentor> getMentorsByClass(String className);
    Set<Mentor> getMentorsBySearchTerm(String searchTerm);
    Mentor getMentorById(int id);

    Mentor getMentorByBasicDataId(int basicDataId);

    void updateMentor(Mentor mentor);
    void addMentor(Mentor mentor);
}
