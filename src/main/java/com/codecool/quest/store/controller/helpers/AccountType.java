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

    public static AccountType fromInt(int i) {
        switch (i) {
            case 1:
                return AccountType.ADMIN;
            case 2:
                return AccountType.MENTOR;
            case 3:
                return AccountType.CODECOOLER;
        }
        return null;
    }
}
