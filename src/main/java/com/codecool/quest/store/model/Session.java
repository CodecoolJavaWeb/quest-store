package com.codecool.quest.store.model;

import com.codecool.quest.store.controller.helpers.AccountType;

public class Session {

    private String sessionId;
    private int basicDataId;
    private AccountType accountType;

    public Session() {
    }

    public Session(String sessionId, int basicDataId, AccountType accountType) {
        this.sessionId = sessionId;
        this.basicDataId = basicDataId;
        this.accountType = accountType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getBasicDataId() {
        return basicDataId;
    }

    public void setBasicDataId(int basicDataId) {
        this.basicDataId = basicDataId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
