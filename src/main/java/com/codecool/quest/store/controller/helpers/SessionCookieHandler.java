package com.codecool.quest.store.controller.helpers;

import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbSessionDAO;
import com.codecool.quest.store.controller.dao.SessionDAO;
import com.codecool.quest.store.model.Session;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.List;

public class SessionCookieHandler {

    private SessionDAO sessionDAO = new DbSessionDAO(new ConnectionFactory().getConnection());
    private Session session = null;

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
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String sessionId = getSessionIdFromCookie(cookieStr);
        session = sessionDAO.getSession(sessionId);

        return session != null && session.getAccountType() == accountType;
    }

    public Session getSession() {
        return session;
    }
}
