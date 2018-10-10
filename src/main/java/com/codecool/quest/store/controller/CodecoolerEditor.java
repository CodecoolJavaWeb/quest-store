package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Codecooler;

import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class CodecoolerEditor implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private ClassDAO classDAO = new DbClassDAO(new ConnectionFactory().getConnection());
    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private Codecooler codecooler = null;
    private Quest quest = null;
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToLoginPage(httpExchange);
        }

        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")){
            handlePost(httpExchange);
        } else {
            handleGet(httpExchange.getRequestURI().getPath());
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handleGet (String URI) {
        String[] URIparts = URI.split("/");
        int codecoolerId = Integer.valueOf(URIparts[URIparts.length - 1]);
        codecooler = codecoolerDAO.getCodecoolerById(codecoolerId);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        codecooler.getBasicUserData().setFirstName(inputs.get("firstName"));
        codecooler.getBasicUserData().setLastName(inputs.get("lastName"));
        codecooler.getBasicUserData().setEmail(inputs.get("email"));
        codecooler.setClassName(inputs.get("className"));
        codecoolerDAO.updateCodecooler(codecooler);
    }

    private String getResponse() {
        String className = codecooler.getClassName();
        List<String> classes = classDAO.getClassesNames();

        List<String> quests = questDAO.getQuestsNames();
     //   List<String> doneQuests = questDAO.getDoneQuestsByCodecooler(codecooler);

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecooler_editor.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("firstName", codecooler.getBasicUserData().getFirstName());
        model.with("lastName", codecooler.getBasicUserData().getLastName());
        model.with("email", codecooler.getBasicUserData().getEmail());
        model.with("className", className);
        model.with("classes", classes);
        model.with("firstName", codecooler.getBasicUserData().getFirstName());
        model.with("lastname", codecooler.getBasicUserData().getLastName());
        model.with("done_quests", quests);
        model.with("done_artifacts", quests);


        return template.render(model);
    }

}
