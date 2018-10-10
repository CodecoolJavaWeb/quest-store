package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;

public class QuestDetail implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private Quest quest = null;
    private Codecooler codecooler = null;
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        byte[] responseBytes = getResponse(httpExchange).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse(HttpExchange httpExchange) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quest_detail.twig");
        int questId = new Utils().getIdFromURI(httpExchange);
        quest = questDAO.getQuestById(questId);
        int basicDataId = sessionCookieHandler.getSession().getBasicDataId();
        codecooler = codecoolerDAO.getCodecoolerByBasicDataId(basicDataId);
        int questCount = questDAO.getCountOfDoneQuestByCodecooler(quest, codecooler);

        JtwigModel model = JtwigModel.newModel();
        model.with("quest", quest);
        model.with("questCount", questCount);
        return template.render(model);
    }
}
