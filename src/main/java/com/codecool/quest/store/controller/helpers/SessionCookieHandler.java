package com.codecool.quest.store.controller.helpers;

import java.net.HttpCookie;
import java.util.List;

public class SessionCookieHandler {

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
}
