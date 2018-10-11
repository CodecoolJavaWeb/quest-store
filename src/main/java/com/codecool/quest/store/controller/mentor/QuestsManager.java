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
import java.util.Set;

public class QuestsManager implements HttpHandler {

    private final String displayStyle = "style=\"display: block;\"";
    private final String questLink = "/quest_editor";
    private final String navLink = "mentor_nav.twig";

    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }

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

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quests.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("navLink", navLink);
        model.with("displayStyle", displayStyle);
        model.with("questLink", questLink);
        model.with("quests", quests);


        return template.render(model);
    }
}
