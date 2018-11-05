package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Logout implements HttpHandler {

    private DAOFactory daoFactory;
    private View view = new View();

    public Logout(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String sessionId = new SessionCookieHandler(daoFactory).getSessionIdFromCookie(cookieStr);
        daoFactory.getSessionDAO().removeSessionById(sessionId);
        view.redirectToPath(httpExchange, "/");

    }
}
