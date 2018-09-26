package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.controller.helpers.AccountType;

public interface LoginDAO {

    String getPasswordByEmail(String email);

    int getIdByEmail(String email);

    AccountType getAccountTypeById(int basicDataId);
}
