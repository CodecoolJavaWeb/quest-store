package com.codecool.quest.store.controller.mentor;

import com.codecool.quest.store.controller.dao.ArtifactDAO;
import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Set;

public class ArtifactsManager implements HttpHandler {

    private final String displayStyle = "style=\"display: block;\"";
    private final String artifactLink = "/artifact_editor";
    private final String navLink = "mentor_nav.twig";

    private ArtifactDAO artifactDAO;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    public ArtifactsManager(DAOFactory daoFactory) {
        this.artifactDAO = daoFactory.getArtifactDAO();
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
