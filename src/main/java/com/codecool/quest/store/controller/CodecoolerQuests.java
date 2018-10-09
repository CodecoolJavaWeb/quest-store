package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbQuestDAO;
import com.codecool.quest.store.controller.dao.QuestDAO;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Set;

public class CodecoolerQuests implements HttpHandler {

    private final String displayStyle = "style=\"display: none;\"";
    private final String questLink = "/quest_detail";

    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToLoginPage(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quests.twig");
        Set<Quest> quests = questDAO.getAllQuests();

        JtwigModel model = JtwigModel.newModel();
        model.with("displayStyle", displayStyle);
        model.with("questLink", questLink);
        model.with("quests", quests);
        return template.render(model);
    }
}
