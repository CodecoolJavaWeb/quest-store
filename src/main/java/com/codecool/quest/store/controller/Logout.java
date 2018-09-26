package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbSessionDAO;
import com.codecool.quest.store.controller.dao.SessionDAO;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Logout implements HttpHandler {

    private SessionDAO sessionDAO = new DbSessionDAO(new ConnectionFactory().getConnection());

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String sessionId = new SessionCookieHandler().getSessionIdFromCookie(cookieStr);
        System.out.println("Session id = " + sessionId);
        sessionDAO.removeSessionById(sessionId);
        httpExchange.getResponseHeaders().set("Location", "/");
        httpExchange.sendResponseHeaders(302, 0);

    }
}
