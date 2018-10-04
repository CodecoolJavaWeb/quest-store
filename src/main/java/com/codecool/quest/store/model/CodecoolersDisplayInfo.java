package com.codecool.quest.store.model;

import java.util.Set;

public class CodecoolersDisplayInfo {

    private Set<Codecooler> codecoolers = null;
    private String className = null;
    private String searchTerm = null;

    public Set<Codecooler> getCodecoolers() {
        return codecoolers;
    }

    public void setCodecoolers(Set<Codecooler> codecoolers) {
        this.codecoolers = codecoolers;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}