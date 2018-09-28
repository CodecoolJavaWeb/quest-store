package com.codecool.quest.store.model;

public class Codecooler  {

    private int id;
    private int exp;
    private int balance;
    private int teamId;
    private int classId;
    private BasicUserData basicUserData;


    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
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

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getId() {
        return id;
    }

    public void setClassId(int classId) {
        this.classId = classId;
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
