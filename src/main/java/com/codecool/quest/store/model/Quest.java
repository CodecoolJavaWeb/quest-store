package com.codecool.quest.store.model;

public class Quest  {

    private int id;
    private String name;
    private String description;
    private int value;
    private boolean isExtra;
    private String imagePath = "";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isExtra() {
        return isExtra;
    }

    public void setExtra(boolean extra) {
        isExtra = extra;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Quest) {
            Quest other = (Quest) obj;
            return this.id == other.getId();
        }
        return false;
    }

    @Override
    public String toString() {
        if (this.isExtra) {
            return "Extra quest - " + this.name;
        } else {
            return "Regular quest - " + this.name;
        }
    }


}
