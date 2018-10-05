package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.FormDataParser;
import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddQuest implements HttpHandler {

    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")){
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new FormDataParser().parseFormData(httpExchange);

        Quest quest = new Quest();
        quest.setName(inputs.get("questName"));
        quest.setDescription(inputs.get("description"));
        quest.setValue(Integer.valueOf(inputs.get("value")));
        quest.setExtra(Boolean.valueOf(inputs.get("questType")));
        questDAO.addQuest(quest);

        httpExchange.getResponseHeaders().set("Location", "/quests_manager");
        httpExchange.sendResponseHeaders(302, 0);
    }

    private String getResponse() {
        List<String> quests = questDAO.getQuestsNames();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/add_new_quest.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("quests", quests);

        return template.render(model);
    }
}
