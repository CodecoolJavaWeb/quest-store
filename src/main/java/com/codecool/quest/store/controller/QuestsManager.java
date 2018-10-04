package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.FormDataParser;
import com.codecool.quest.store.model.MentorsDisplayInfo;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class QuestsManager implements HttpHandler {

    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }



    private String getResponse() {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quests/quests_manager.twig");
        JtwigModel model = JtwigModel.newModel();


        return template.render(model);
    }
}
