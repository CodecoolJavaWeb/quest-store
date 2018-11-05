package com.codecool.quest.store.controller.admin;

import com.codecool.quest.store.controller.dao.DAOFactory;
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

    private DAOFactory daoFactory;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler;

    public MentorEditor(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.sessionCookieHandler = new SessionCookieHandler(daoFactory);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.ADMIN)) {
            view.redirectToPath(httpExchange, "/");
        }

        Mentor mentor = getMentor(httpExchange);

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")){
            handlePost(httpExchange, mentor);
        }

        byte[] responseBytes = getResponse(mentor).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private Mentor getMentor(HttpExchange httpExchange) {
        int mentorId = new Utils().getIdFromURI(httpExchange);
        return daoFactory.getMentorDAO().getMentorById(mentorId);
    }

    private void handlePost(HttpExchange httpExchange, Mentor mentor) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        mentor.getBasicUserData().setFirstName(inputs.get("firstName"));
        mentor.getBasicUserData().setLastName(inputs.get("lastName"));
        mentor.getBasicUserData().setEmail(inputs.get("email"));
        mentor.setClassName(inputs.get("className"));
        daoFactory.getMentorDAO().updateMentor(mentor);
    }

    private String getResponse(Mentor mentor) {
        String className = mentor.getClassName();
        Set<Codecooler> codecoolersInClass = daoFactory.getCodecoolerDAO().getCodecoolersByClassName(className);
        List<String> classes = daoFactory.getClassDAO().getClassesNames();

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
