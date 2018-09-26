package com.codecool.quest.store.controller.dao;

public interface LoginDAO {

    String getPasswordByEmail(String email);

    int getIdByEmail(String email);

    String getAccountTypeById(int basicDataId);
}
