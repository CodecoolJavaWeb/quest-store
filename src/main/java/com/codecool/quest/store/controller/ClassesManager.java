package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.ClassDAO;
import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbClassDAO;
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

    private ClassDAO classDAO = new DbClassDAO(new ConnectionFactory().getConnection());
    private View view = new View();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
