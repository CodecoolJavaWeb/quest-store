package com.codecool.quest.store.controller.mentor;

import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbQuestDAO;
import com.codecool.quest.store.controller.dao.QuestDAO;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Map;


public class QuestEditor implements HttpHandler {

    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private Quest quest = null;
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")){
            handlePost(httpExchange);
        } else {
            handleGet(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handleGet (HttpExchange httpExchange) {
        int questId = new Utils().getIdFromURI(httpExchange);
        quest = questDAO.getQuestById(questId);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        quest.setName(inputs.get("questName"));
        quest.setDescription(inputs.get("description"));
        quest.setValue(Integer.valueOf(inputs.get("value")));
        quest.setExtra(Boolean.valueOf(inputs.get("isExtra")));

        questDAO.updateQuest(quest);

    }

    private String getResponse() {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quest_editor.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("questName", quest.getName());
        model.with("description", quest.getDescription());
        model.with("value", quest.getValue());
        model.with("isExtra", quest.isExtra());


        return template.render(model);
    }
}
