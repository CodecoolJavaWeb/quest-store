package com.codecool.quest.store.controller.dao;

import java.util.List;

public interface ClassDAO {
    List<String> getClassesNames();
    void addClass(String className);
    void deleteClass(String className);
}
