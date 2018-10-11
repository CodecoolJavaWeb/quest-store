package com.codecool.quest.store.controller.admin;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.model.Mentor;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MentorEditor implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private MentorDAO mentorDAO = new DbMentorDAO(new ConnectionFactory().getConnection());
    private ClassDAO classDAO = new DbClassDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private Mentor mentor = null;
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.ADMIN)) {
            view.redirectToPath(httpExchange, "/");
        }


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
        int mentorId = new Utils().getIdFromURI(httpExchange);
        mentor = mentorDAO.getMentorById(mentorId);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        mentor.getBasicUserData().setFirstName(inputs.get("firstName"));
        mentor.getBasicUserData().setLastName(inputs.get("lastName"));
        mentor.getBasicUserData().setEmail(inputs.get("email"));
        mentor.setClassName(inputs.get("className"));
        mentorDAO.updateMentor(mentor);
    }

    private String getResponse() {
        String className = mentor.getClassName();
        Set<Codecooler> codecoolersInClass = codecoolerDAO.getCodecoolersByClassName(className);
        List<String> classes = classDAO.getClassesNames();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor_editor.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("firstName", mentor.getBasicUserData().getFirstName());
        model.with("lastName", mentor.getBasicUserData().getLastName());
        model.with("email", mentor.getBasicUserData().getEmail());
        model.with("className", className);
        model.with("classes", classes);
        model.with("codecoolers", codecoolersInClass);

        return template.render(model);
    }

}
