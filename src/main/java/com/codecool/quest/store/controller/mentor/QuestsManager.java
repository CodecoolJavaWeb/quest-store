package com.codecool.quest.store.controller.mentor;

import com.codecool.quest.store.controller.dao.DAOFactory;
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

public class QuestsManager implements HttpHandler {

    private final String displayStyle = "style=\"display: block;\"";
    private final String questLink = "/quest_editor";
    private final String navLink = "mentor_nav.twig";

    private DAOFactory daoFactory;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler;

    public QuestsManager(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.sessionCookieHandler = new SessionCookieHandler(daoFactory);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse() {
        Set<Quest> quests = daoFactory.getQuestDAO().getAllQuests();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quests.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("navLink", navLink);
        model.with("displayStyle", displayStyle);
        model.with("questLink", questLink);
        model.with("quests", quests);


        return template.render(model);
    }
}
