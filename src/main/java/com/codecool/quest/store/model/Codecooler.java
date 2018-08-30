package com.codecool.quest.store.model;

public class Codecooler extends User {

    private int exp;
    private int balance;
    private int teamId;

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

    @Override
    public String toString() {
        return String.format("%d. %s %s - %s - class: %d - exp: %d - balance: %d - team: %d",
                getId(),
                getFirstName(),
                getLastName(),
                getEmail(),
                getClassId(),
                this.exp,
                this.balance,
                this.teamId);
    }
}
