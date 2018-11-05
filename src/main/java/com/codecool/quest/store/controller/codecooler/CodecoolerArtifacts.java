package com.codecool.quest.store.controller.codecooler;

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

public class CodecoolerArtifacts implements HttpHandler {

    private final String displayStyle = "style=\"display: none;\"";
    private final String artifactLink = "/buy_artifact";
    private final String navLink = "codecooler_nav.twig";

    private DAOFactory daoFactory;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler;

    public CodecoolerArtifacts(DAOFactory daoFactory) {
        this.daoFactory= daoFactory;
        this.sessionCookieHandler = new SessionCookieHandler(daoFactory);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifacts.twig");
        Set<Artifact> artifacts = daoFactory.getArtifactDAO().getAllArtifacts();

        JtwigModel model = JtwigModel.newModel();
        model.with("displayStyle", displayStyle);
        model.with("artifactLink", artifactLink);
        model.with("artifacts", artifacts);
        model.with("navLink", navLink);
        return template.render(model);
    }
}
