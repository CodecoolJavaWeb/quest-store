package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;

public class BuyArtifact implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
    private Codecooler codecooler = null;
    private Artifact artifact = null;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse(httpExchange).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        artifactDAO.addArtifactToBought(artifact, codecooler);
        int balance = codecooler.getBalance();
        int price = artifact.getPrice();
        codecooler.setBalance(balance - price);
        codecoolerDAO.updateCodecooler(codecooler);
        view.redirectToPath(httpExchange,"/codecooler_artifacts");
    }

    private String getResponse(HttpExchange httpExchange) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/buy_artifact.twig");
        int basicDataId = sessionCookieHandler.getSession().getBasicDataId();
        codecooler = codecoolerDAO.getCodecoolerByBasicDataId(basicDataId);
        int artifactId = new Utils().getIdFromURI(httpExchange);
        artifact = artifactDAO.getArtifactById(artifactId);

        JtwigModel model = JtwigModel.newModel();
        model.with("codecooler", codecooler);
        model.with("artifact", artifact);
        model.with("tooExpensive", codecooler.getBalance() < artifact.getPrice());
        return template.render(model);
    }
}
