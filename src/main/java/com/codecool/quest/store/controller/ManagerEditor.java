package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.model.Codecooler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public class ManagerEditor implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

    }
    private void showCodecoolersInClass(HttpExchange httpExchange, String codecoolers) throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("template/mentor_editor.twig");

        Set<Codecooler> codecoolersInClass = codecoolerDAO.getCodecoolersByClassId(1);
        JtwigModel model = JtwigModel.newModel();
        model.with("codecoolers", codecoolersInClass);

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
