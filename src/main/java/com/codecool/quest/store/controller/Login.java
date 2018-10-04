package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.FormDataParser;
import com.codecool.quest.store.model.Session;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Map;
import java.util.UUID;

public class Login implements HttpHandler {

    private LoginDAO loginDAO = new DbLoginDAO(new ConnectionFactory().getConnection());
    private SessionDAO sessionDAO = new DbSessionDAO(new ConnectionFactory().getConnection());

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            sendLoginPage(httpExchange, "");
        }

        if (method.equals("POST")) {
            handleLogin(httpExchange);
        }
    }

    private void sendLoginPage(HttpExchange httpExchange, String errMessage) throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");

        JtwigModel model = JtwigModel.newModel();
        model.with("errMessage", errMessage);

        String response = template.render(model);
        byte[] responseBytes = response.getBytes();
        new View().sendResponse(httpExchange, responseBytes);
    }

    private void handleLogin (HttpExchange httpExchange) throws IOException {

        Map<String, String> inputs = new FormDataParser().parseFormData(httpExchange);

        if (isPasswordCorrect(inputs)) {

            String sessionId = UUID.randomUUID().toString();
            Session session = createNewSession(inputs, sessionId);

            redirectToMainPage(httpExchange, session);
        } else {
            sendLoginPage(httpExchange, "Incorrect email or password");
        }
    }

    private boolean isPasswordCorrect(Map<String, String> inputs) {
        String email = inputs.get("email");
        String givenPassword = inputs.get("password");
        String savedPassword = loginDAO.getPasswordByEmail(email);
        return givenPassword.equals(savedPassword);
    }

    private Session createNewSession (Map<String, String> inputs, String sessionId) {
        String email = inputs.get("email");
        int basicDataId = loginDAO.getIdByEmail(email);
        AccountType accountType = loginDAO.getAccountTypeById(basicDataId);
        Session session = new Session(sessionId, basicDataId, accountType);
        sessionDAO.addSession(session);
        return session;
    }

    private void redirectToMainPage(HttpExchange httpExchange, Session session) throws IOException {
        HttpCookie cookie = new HttpCookie("sessionId", session.getSessionId());
        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.add("Set-Cookie", cookie.toString());

        switch (session.getAccountType()) {
            case ADMIN:
                responseHeaders.set("Location", "/mentors_manager");
                break;
            case MENTOR:
                responseHeaders.set("Location", "/codecoolers_manager");
                break;
            case CODECOOLER:
                responseHeaders.set("Location", "/codecooler");
                break;
        }

        httpExchange.sendResponseHeaders(302, 0);
    }
}
