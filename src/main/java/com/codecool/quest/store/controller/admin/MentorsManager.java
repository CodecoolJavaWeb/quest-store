package com.codecool.quest.store.controller.admin;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.MentorsDisplayInfo;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MentorsManager implements HttpHandler {

    private MentorDAO mentorDAO = new DbMentorDAO(new ConnectionFactory().getConnection());
    private ClassDAO classDAO = new DbClassDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.ADMIN)) {
            view.redirectToPath(httpExchange, "/");
        }

        MentorsDisplayInfo displayInfo = new MentorsDisplayInfo();

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            handlePost(httpExchange, displayInfo);
        }

        byte[] responseBytes = getResponse(displayInfo).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange, MentorsDisplayInfo displayInfo) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        if (inputs.containsKey("show_all")) {
            displayInfo.setMentors(mentorDAO.getAllMentors());

        } else if (inputs.containsKey("show_class")) {
            displayInfo.setClassName(inputs.get("class"));
            displayInfo.setMentors(mentorDAO.getMentorsByClass(displayInfo.getClassName()));

        } else if (inputs.containsKey("search")) {
            displayInfo.setSearchTerm(inputs.get("search_field"));
            displayInfo.setMentors(mentorDAO.getMentorsBySearchTerm(displayInfo.getSearchTerm()));
        }
    }

    private String getResponse(MentorsDisplayInfo displayInfo) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentors_manager.twig");
        JtwigModel model = JtwigModel.newModel();
        List<String> classes = classDAO.getClassesNames();
        model.with("classes", classes);
        model.with("mentors", displayInfo.getMentors());
        model.with("className", displayInfo.getClassName());
        model.with("searchTerm", displayInfo.getSearchTerm());
        return template.render(model);
    }
}
