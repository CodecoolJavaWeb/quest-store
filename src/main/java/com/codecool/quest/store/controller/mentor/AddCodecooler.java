package com.codecool.quest.store.controller.mentor;

import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.BasicUserData;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddCodecooler implements HttpHandler {

    private DAOFactory daoFactory;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler;

    public AddCodecooler(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.sessionCookieHandler = new SessionCookieHandler(daoFactory);
    }

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

        Codecooler codecooler = new Codecooler();
        BasicUserData basicUserData = new BasicUserData();
        basicUserData.setFirstName(inputs.get("firstName"));
        basicUserData.setLastName(inputs.get("lastName"));
        basicUserData.setEmail(inputs.get("email"));
        basicUserData.setPassword("password");
        codecooler.setBasicUserData(basicUserData);
        codecooler.setClassName(inputs.get("className"));
        daoFactory.getCodecoolerDAO().addCodecooler(codecooler);

        httpExchange.getResponseHeaders().set("Location", "/codecoolers_manager");
        httpExchange.sendResponseHeaders(302, 0);
    }

    private String getResponse() {
        List<String> classes = daoFactory.getClassDAO().getClassesNames();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/add_new_codecooler.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("classes", classes);

        return template.render(model);
    }
}
