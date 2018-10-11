package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.ArtifactDAO;
import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbArtifactDAO;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Map;


public class ArtifactEditor implements HttpHandler {

    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private Artifact artifact = null;


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
        int artifactId = new Utils().getIdFromURI(httpExchange);
        artifact = artifactDAO.getArtifactById(artifactId);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        artifact.setName(inputs.get("artifactName"));
        artifact.setDescription(inputs.get("description"));
        artifact.setPrice(Integer.valueOf(inputs.get("price")));
        artifact.setMagic(Boolean.valueOf(inputs.get("isMagic")));

        artifactDAO.updateArtifact(artifact);

    }

    private String getResponse() {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifact_editor.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("artifactName", artifact.getName());
        model.with("description", artifact.getDescription());
        model.with("price", artifact.getPrice());
        model.with("isMagic", artifact.isMagic());


        return template.render(model);
    }
}
