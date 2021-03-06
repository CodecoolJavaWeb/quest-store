package com.codecool.quest.store.model;

public class Artifact {

    private int id;
    private String name;
    private String description;
    private int price;
    private boolean isMagic;
    private String imagePath;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isMagic() {
        return isMagic;
    }

    public void setMagic(boolean magic) {
        isMagic = magic;
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
        if (obj instanceof Artifact) {
            Artifact other = (Artifact) obj;
            return this.id == other.getId();
        }
        return false;
    }

    @Override
    public String toString() {
        if (this.isMagic) {
            return "Magic artifact - " + this.name;
        } else {
            return "Regular artifact - " + this.name;
        }
    }
}
