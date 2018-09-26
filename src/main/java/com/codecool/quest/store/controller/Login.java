package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.model.Session;
import com.codecool.quest.store.controller.helpers.FormDataParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
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
            Map<String, String> inputs = new FormDataParser().parseFormData(httpExchange);
            String email = inputs.get("email");
            String givenPassword = inputs.get("password");


            String savedPassword = loginDAO.getPasswordByEmail(email);

            if (givenPassword.equals(savedPassword)) {

                String sessionId = UUID.randomUUID().toString();
                int basicDataId = loginDAO.getIdByEmail(email);
                String accountType = loginDAO.getAccountTypeById(basicDataId);

                sessionDAO.addSession(new Session(sessionId, basicDataId, accountType));

                HttpCookie cookie = new HttpCookie("sessionId", sessionId);
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());

                Headers responseHeaders = httpExchange.getResponseHeaders();

                switch (accountType) {
                    case "admin":
                        responseHeaders.set("Location", "/static/mentors_manager.html");
                        break;
                    case "mentor":
                        responseHeaders.set("Location", "/mentor");
                        break;
                    case "codecooler":
                        responseHeaders.set("Location", "/codecooler");
                        break;
                }

                httpExchange.sendResponseHeaders(302, 0);
            } else {
                sendLoginPage(httpExchange, "Incorrect email or password");
            }

        }
    }

    private void sendLoginPage(HttpExchange httpExchange, String errMessage) throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");

        JtwigModel model = JtwigModel.newModel();
        model.with("errMessage", errMessage);

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
