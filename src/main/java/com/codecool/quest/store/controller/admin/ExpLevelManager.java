package com.codecool.quest.store.controller.admin;

import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.dao.LevelDAO;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Level;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ExpLevelManager implements HttpHandler {

    private LevelDAO levelDAO;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    public ExpLevelManager(DAOFactory daoFactory) {
        this.levelDAO = daoFactory.getLevelDAO();
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

        if (inputs.containsKey("save")) {
            levelDAO.updateLevel(createLevelFromInputs(inputs));
        } else if (inputs.containsKey("add")) {
            levelDAO.addLevel(createLevelFromInputs(inputs));
        } else if (inputs.containsKey("delete")) {
            levelDAO.deleteLevel(Integer.valueOf(inputs.get("levelId")));
        }
    }

    private Level createLevelFromInputs(Map<String, String> inputs) {
        Level level = new Level();
        if (inputs.containsKey("levelId")) {
            level.setId(Integer.valueOf(inputs.get("levelId")));
        }
        level.setName(inputs.get("levelName"));
        level.setStartValue(Integer.valueOf(inputs.get("startValue")));
        level.setEndValue(Integer.valueOf(inputs.get("endValue")));
        return level;
    }

    private String getResponse() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/exp_level_manager.twig");
        JtwigModel model = JtwigModel.newModel();
        Set<Level> levels = levelDAO.getAllLevels();
        model.with("levels", levels);
        return template.render(model);
    }
}
