package com.codecool.quest.store.controller.admin;

import com.codecool.quest.store.controller.dao.ClassDAO;
import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClassesManager implements HttpHandler {

    private ClassDAO classDAO;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    public ClassesManager(DAOFactory daoFactory) {
        this.classDAO = daoFactory.getClassDAO();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.ADMIN)) {
            view.redirectToPath(httpExchange, "/");
        }

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        if (inputs.containsKey("delete")) {
            deleteClasses(inputs);
        } else if (inputs.containsKey("add")) {
            classDAO.addClass(inputs.get("className"));
        }
    }

    private void deleteClasses(Map<String, String> inputs) {
        inputs.keySet().forEach(key -> {
            if (!key.equals("delete")) {
                classDAO.deleteClass(inputs.get(key));
            }
        });
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/classes_manager.twig");
        JtwigModel model = JtwigModel.newModel();
        List<String> classes = classDAO.getClassesNames();
        model.with("classes", classes);
        return template.render(model);
    }
}
