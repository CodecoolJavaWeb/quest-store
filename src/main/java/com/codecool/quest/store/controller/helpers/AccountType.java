package com.codecool.quest.store.controller.helpers;

public enum AccountType {
    ADMIN (1),
    MENTOR (2),
    CODECOOLER (3);

    private final int value;

    AccountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
