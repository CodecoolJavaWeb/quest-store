package com.codecool.quest.store.controller.helpers;

import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.model.Session;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.List;

public class SessionCookieHandler {

    private DAOFactory daoFactory;


    public SessionCookieHandler(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public String getSessionIdFromCookie(String cookieStr) {
        String[] cookieStrings = cookieStr.split("; ");
        List<HttpCookie> cookies = null;
        for (String str: cookieStrings) {
            if (str.contains("sessionId")) {
                cookies = HttpCookie.parse(str);
            }
        }
        if (cookies != null) {
            return cookies.get(0).getValue();
        }
        return "";
    }

    public boolean isSessionValid(HttpExchange httpExchange, AccountType accountType) {
        Session session = getSession(httpExchange);
        return session != null && session.getAccountType() == accountType;
    }

    public Session getSession(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String sessionId = getSessionIdFromCookie(cookieStr);
        return daoFactory.getSessionDAO().getSession(sessionId);
    }
}
