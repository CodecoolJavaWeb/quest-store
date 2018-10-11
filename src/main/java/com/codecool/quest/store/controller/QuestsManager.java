package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class QuestsManager implements HttpHandler {

    private final String displayStyle = "style=\"display: block;\"";
    private final String questLink = "/quest_editor";

    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();


        if (method.equals("POST")) {
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

    }



    private String getResponse() {
        Set<Quest> quests = questDAO.getAllQuests();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quests_manager.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("displayStyle", displayStyle);
        model.with("questLink", questLink);
        model.with("quests", quests);


        return template.render(model);
    }
}
