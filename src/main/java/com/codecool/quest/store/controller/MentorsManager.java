package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DbMentorDAO;
import com.codecool.quest.store.controller.dao.MentorDAO;
import com.codecool.quest.store.controller.helpers.FormDataParser;
import com.codecool.quest.store.model.Mentor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

public class MentorsManager implements HttpHandler {

    MentorDAO mentorDAO = new DbMentorDAO(new ConnectionFactory().getConnection());

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentors_manager.twig");
        JtwigModel model = JtwigModel.newModel();
        Set<Mentor> mentors = null;

        if (method.equals("POST")) {
            Map<String, String> inputs = new FormDataParser().parseFormData(httpExchange);

            if (inputs.containsKey("show_all")) {
                mentors = mentorDAO.getAllMentors();
            }

        }

        model.with("mentors", mentors);
        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
