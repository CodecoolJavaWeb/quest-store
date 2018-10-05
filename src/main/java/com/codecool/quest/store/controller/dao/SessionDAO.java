package com.codecool.quest.store.controller.dao;

import com.codecool.quest.store.model.Session;

public interface SessionDAO {
    void addSession(Session session);
    void removeSessionById(String sessionId);
    Session getSession(String sessionId);
}
