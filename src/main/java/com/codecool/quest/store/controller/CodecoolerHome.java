package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
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

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private LevelDAO levelDAO = new DbLevelDAO(new ConnectionFactory().getConnection());
    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private Codecooler codecooler = null;
    private Set<Artifact> artifacts = null;
    private Set<Quest> quests = null;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecooler_home.twig");
        int basicDataId = sessionCookieHandler.getSession().getBasicDataId();
        codecooler = codecoolerDAO.getCodecoolerByBasicDataId(basicDataId);
        String levelName = levelDAO.getLevelNameByValue(codecooler.getExp());
        artifacts = artifactDAO.getBoughtArtifactsByCodecooler(codecooler);
        quests = questDAO.getDoneQuestsByCodecooler(codecooler);

        JtwigModel model = JtwigModel.newModel();
        model.with("codecooler", codecooler);
        model.with("levelName", levelName);
        model.with("artifacts", artifacts);
        model.with("quests", quests);
        return template.render(model);
    }
}
