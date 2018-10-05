package com.codecool.quest.store.model;

public class Codecooler  {

    private int id;
    private int exp;
    private int balance;
    private String teamName;
    private String className;
    private BasicUserData basicUserData;


    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }


    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        if (teamName == null) {
            this.teamName = "";
        } else {
            this.teamName = teamName;
        }
    }

    public int getId() {
        return id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public BasicUserData getBasicUserData() {
        return basicUserData;
    }

    public void setBasicUserData(BasicUserData basicUserData) {
        this.basicUserData = basicUserData;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Codecooler) {
            Codecooler other = (Codecooler) obj;
            return this.id == other.getId();
        }
        return false;
    }

    @Override
    public String toString() {
        return basicUserData.getFirstName() + " " + basicUserData.getLastName() + " - " + basicUserData.getEmail();
    }
}
