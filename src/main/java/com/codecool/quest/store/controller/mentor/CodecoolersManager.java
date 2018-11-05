package com.codecool.quest.store.controller.mentor;

import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.CodecoolersDisplayInfo;
import com.codecool.quest.store.model.Mentor;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CodecoolersManager implements HttpHandler {

    private DAOFactory daoFactory;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler;

    public CodecoolersManager(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.sessionCookieHandler = new SessionCookieHandler(daoFactory);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }



        int basicDataId = sessionCookieHandler.getSession(httpExchange).getBasicDataId();
        Mentor mentor = daoFactory.getMentorDAO().getMentorByBasicDataId(basicDataId);
        CodecoolersDisplayInfo displayInfo = new CodecoolersDisplayInfo();

        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")) {
            handlePost(httpExchange, displayInfo, mentor);
        }

        byte[] responseBytes = getResponse(displayInfo).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange, CodecoolersDisplayInfo displayInfo, Mentor mentor) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        String className = mentor.getClassName();

        if (inputs.containsKey("show_mentor_students")) {
            displayInfo.setCodecoolers(daoFactory.getCodecoolerDAO().getCodecoolersByClassName(className));

        } else if (inputs.containsKey("show_all_students")) {
            displayInfo.setCodecoolers(daoFactory.getCodecoolerDAO().getAllCodecoolers());

        } else if (inputs.containsKey("search")) {
            displayInfo.setSearchTerm(inputs.get("search_field"));
            displayInfo.setCodecoolers(daoFactory.getCodecoolerDAO().getCodecoolersBySearchTerm(displayInfo.getSearchTerm()));
        }
    }

    private String getResponse(CodecoolersDisplayInfo displayInfo) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecoolers_manager.twig");
        JtwigModel model = JtwigModel.newModel();
        List<String> classes = daoFactory.getClassDAO().getClassesNames();
        model.with("classes", classes);
        model.with("codecoolers", displayInfo.getCodecoolers());
        model.with("className", displayInfo.getClassName());
        model.with("searchTerm", displayInfo.getSearchTerm());
        return template.render(model);
    }
}
