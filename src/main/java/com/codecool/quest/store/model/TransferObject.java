package com.codecool.quest.store.model;

public class TransferObject {

    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof TransferObject) {
            TransferObject other = (TransferObject) obj;
            return this.id == other.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
