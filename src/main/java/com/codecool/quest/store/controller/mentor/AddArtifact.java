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

public class AddArtifact implements HttpHandler {

    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }


        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")){
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        Artifact artifact = new Artifact();
        artifact.setName(inputs.get("artifactName"));
        artifact.setDescription(inputs.get("description"));
        artifact.setPrice(Integer.valueOf(inputs.get("price")));
        artifact.setMagic(Boolean.valueOf(inputs.get("artifactType")));
        artifactDAO.addArtifact(artifact);

        httpExchange.getResponseHeaders().set("Location", "/artifacts_manager");
        httpExchange.sendResponseHeaders(302, 0);
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/add_new_artifact.twig");
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }
}
