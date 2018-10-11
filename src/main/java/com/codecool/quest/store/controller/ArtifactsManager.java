package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ArtifactsManager implements HttpHandler {

    private final String displayStyle = "style=\"display: block;\"";
    private final String artifactLink = "/artifact_editor";
    private final String navLink = "mentor_nav.twig";

    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
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
        Set<Artifact> artifacts = artifactDAO.getAllArtifacts();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifacts.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("navLink", navLink);
        model.with("displayStyle", displayStyle);
        model.with("artifactLink", artifactLink);
        model.with("artifacts", artifacts);


        return template.render(model);
    }
}
