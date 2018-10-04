package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.FormDataParser;
import com.codecool.quest.store.model.CodecoolersDisplayInfo;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CodecoolersManager implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private ClassDAO classDAO = new DbClassDAO(new ConnectionFactory().getConnection());
    private CodecoolersDisplayInfo displayInfo = null;
    private View view = new View();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        displayInfo = new CodecoolersDisplayInfo();

        if (method.equals("POST")) {
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new FormDataParser().parseFormData(httpExchange);

        if (inputs.containsKey("show_mentor_students")) {
            displayInfo.setCodecoolers(codecoolerDAO.getAllCodecoolers()); // póki co wyświetla wszystkich studentów

        } else if (inputs.containsKey("show_all_students")) {
            displayInfo.setCodecoolers(codecoolerDAO.getAllCodecoolers());

        } else if (inputs.containsKey("search")) {
            displayInfo.setSearchTerm(inputs.get("search_field"));
            displayInfo.setCodecoolers(codecoolerDAO.getCodecoolersBySearchTerm(displayInfo.getSearchTerm()));
        }
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecoolers_manager.twig");
        JtwigModel model = JtwigModel.newModel();
        List<String> classes = classDAO.getClassesNames();
        model.with("classes", classes);
        model.with("codecoolers", displayInfo.getCodecoolers());
        model.with("className", displayInfo.getClassName());
        model.with("searchTerm", displayInfo.getSearchTerm());
        return template.render(model);
    }
}
