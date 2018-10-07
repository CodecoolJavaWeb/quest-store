package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.ArtifactDAO;
import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbArtifactDAO;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Set;

public class CodecoolerArtifacts implements HttpHandler {

    private final String displayStyle = "style=\"display: none;\"";
    private final String artifactLink = "/buy_artifact/";

    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifacts.twig");
        Set<Artifact> artifacts = artifactDAO.getAllArtifacts();

        JtwigModel model = JtwigModel.newModel();
        model.with("displayStyle", displayStyle);
        model.with("artifactLink", artifactLink);
        model.with("artifacts", artifacts);
        return template.render(model);
    }
}
