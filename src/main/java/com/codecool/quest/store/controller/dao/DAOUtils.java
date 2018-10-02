package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.BasicUserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOUtils {

    public BasicUserData extractBasicUserDataFromResultSet(ResultSet resultSet) throws SQLException {
        BasicUserData basicUserData = new BasicUserData();
        basicUserData.setFirstName(resultSet.getString("first_name"));
        basicUserData.setLastName(resultSet.getString("last_name"));
        basicUserData.setEmail(resultSet.getString("email"));
        basicUserData.setPassword(resultSet.getString("password"));
        return basicUserData;
    }
}
