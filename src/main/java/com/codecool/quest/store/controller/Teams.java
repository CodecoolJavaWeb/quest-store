package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.FormDataParser;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
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

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private TeamDAO teamDAO = new DbTeamDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();
    private Codecooler codecooler;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToLoginPage(httpExchange);
        }

        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")) {
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new FormDataParser().parseFormData(httpExchange);

        if (inputs.containsKey("add")) {
            Team team = new Team();
            team.setName(inputs.get("newTeamName"));
            team.setClassName(codecooler.getClassName());
            teamDAO.addTeam(team);
        } else if (inputs.containsKey("join")) {
            codecooler.setTeamName(inputs.get("join"));
            codecoolerDAO.updateCodecooler(codecooler);
        }
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/teams.twig");
        int basicDataId = sessionCookieHandler.getSession().getBasicDataId();
        codecooler = codecoolerDAO.getCodecoolerByBasicDataId(basicDataId);
        Set<Team> teams = teamDAO.getTeamsByClassName(codecooler.getClassName());

        JtwigModel model = JtwigModel.newModel();
        model.with("currentTeam", codecooler.getTeamName());
        model.with("teams",teams );
        return template.render(model);
    }
}
