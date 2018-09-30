package com.codecool.quest.store.model;

import java.util.Set;

public class MentorsDisplayInfo {

    private Set<Mentor> mentors = null;
    private String className = null;
    private String searchTerm = null;

    public Set<Mentor> getMentors() {
        return mentors;
    }

    public void setMentors(Set<Mentor> mentors) {
        this.mentors = mentors;
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
