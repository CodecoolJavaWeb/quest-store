package com.codecool.quest.store.model;

public class Mentor {
    private int id;
    private String className;
    private BasicUserData basicUserData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
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
        if (obj instanceof Mentor) {
            Mentor other = (Mentor) obj;
            return this.id == other.getId();
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s",
                basicUserData.getFirstName(),
                basicUserData.getLastName(),
                basicUserData.getEmail());
    }
}
