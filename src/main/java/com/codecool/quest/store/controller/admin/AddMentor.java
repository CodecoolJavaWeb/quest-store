package com.codecool.quest.store.controller.admin;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.BasicUserData;
import com.codecool.quest.store.model.Mentor;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddMentor implements HttpHandler {

    private MentorDAO mentorDAO = new DbMentorDAO(new ConnectionFactory().getConnection());
    private ClassDAO classDAO = new DbClassDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.ADMIN)) {
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

        Mentor mentor = new Mentor();
        BasicUserData basicUserData = new BasicUserData();
        basicUserData.setFirstName(inputs.get("firstName"));
        basicUserData.setLastName(inputs.get("lastName"));
        basicUserData.setEmail(inputs.get("email"));
        basicUserData.setPassword("password");
        mentor.setBasicUserData(basicUserData);
        mentor.setClassName(inputs.get("className"));
        mentorDAO.addMentor(mentor);

        httpExchange.getResponseHeaders().set("Location", "/mentors_manager");
        httpExchange.sendResponseHeaders(302, 0);
    }

    private String getResponse() {
        List<String> classes = classDAO.getClassesNames();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/add_new_mentor.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("classes", classes);

        return template.render(model);
    }
}
