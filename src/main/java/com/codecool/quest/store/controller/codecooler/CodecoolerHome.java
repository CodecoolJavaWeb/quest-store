package com.codecool.quest.store.controller.codecooler;

import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Set;

public class CodecoolerHome implements HttpHandler {

    private DAOFactory daoFactory;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler;

    public CodecoolerHome(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.sessionCookieHandler = new SessionCookieHandler(daoFactory);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        byte[] responseBytes = getResponse(httpExchange).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse(HttpExchange httpExchange) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecooler_home.twig");
        int basicDataId = sessionCookieHandler.getSession(httpExchange).getBasicDataId();
        Codecooler codecooler = daoFactory.getCodecoolerDAO().getCodecoolerByBasicDataId(basicDataId);
        String levelName = daoFactory.getLevelDAO().getLevelNameByValue(codecooler.getExp());
        Set<Artifact> artifacts = daoFactory.getArtifactDAO().getBoughtArtifactsByCodecooler(codecooler);
        Set<Quest> quests = daoFactory.getQuestDAO().getDoneQuestsByCodecooler(codecooler);

        JtwigModel model = JtwigModel.newModel();
        model.with("codecooler", codecooler);
        model.with("levelName", levelName);
        model.with("artifacts", artifacts);
        model.with("quests", quests);
        return template.render(model);
    }
}
