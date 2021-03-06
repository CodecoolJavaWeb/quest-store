package com.codecool.quest.store.controller.codecooler;

import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.model.Team;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Teams implements HttpHandler {

    private DAOFactory daoFactory;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler;

    public Teams(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.sessionCookieHandler = new SessionCookieHandler(daoFactory);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        Codecooler codecooler = getCodecooler(httpExchange);

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            handlePost(httpExchange, codecooler);
        }

        byte[] responseBytes = getResponse(codecooler).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private Codecooler getCodecooler(HttpExchange httpExchange) {
        int basicDataId = sessionCookieHandler.getSession(httpExchange).getBasicDataId();
        return daoFactory.getCodecoolerDAO().getCodecoolerByBasicDataId(basicDataId);
    }

    private void handlePost(HttpExchange httpExchange, Codecooler codecooler) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        if (inputs.containsKey("add")) {
            Team team = new Team();
            team.setName(inputs.get("newTeamName"));
            team.setClassName(codecooler.getClassName());
            daoFactory.getTeamDAO().addTeam(team);
        } else if (inputs.containsKey("join")) {
            codecooler.setTeamName(inputs.get("join"));
            daoFactory.getCodecoolerDAO().updateCodecooler(codecooler);
        }
    }

    private String getResponse(Codecooler codecooler) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/teams.twig");
        Set<Team> teams = daoFactory.getTeamDAO().getTeamsByClassName(codecooler.getClassName());

        JtwigModel model = JtwigModel.newModel();
        model.with("currentTeam", codecooler.getTeamName());
        model.with("teams",teams );
        return template.render(model);
    }
}
