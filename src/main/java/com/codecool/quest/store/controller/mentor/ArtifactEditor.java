package com.codecool.quest.store.controller.mentor;

import com.codecool.quest.store.controller.dao.ArtifactDAO;
import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbArtifactDAO;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
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
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        int artifactId = new Utils().getIdFromURI(httpExchange);
        Artifact artifact = artifactDAO.getArtifactById(artifactId);


        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")){
            handlePost(httpExchange, artifact);
        }

        byte[] responseBytes = getResponse(artifact).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }


    private void handlePost(HttpExchange httpExchange, Artifact artifact) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        artifact.setName(inputs.get("artifactName"));
        artifact.setDescription(inputs.get("description"));
        artifact.setPrice(Integer.valueOf(inputs.get("price")));
        artifact.setMagic(Boolean.valueOf(inputs.get("isMagic")));

        artifactDAO.updateArtifact(artifact);

    }

    private String getResponse(Artifact artifact) {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifact_editor.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("artifactName", artifact.getName());
        model.with("description", artifact.getDescription());
        model.with("price", artifact.getPrice());
        model.with("isMagic", artifact.isMagic());


        return template.render(model);
    }
}
