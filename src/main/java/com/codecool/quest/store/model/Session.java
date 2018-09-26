package com.codecool.quest.store.model;

public class Session {

    private String sessionId;
    private int basicDataId;
    private String accountType;

    public Session() {
    }

    public Session(String sessionId, int basicDataId, String accountType) {
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
