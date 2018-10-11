package com.codecool.quest.store.controller.mentor;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.model.Quest;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CodecoolerEditor implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private ClassDAO classDAO = new DbClassDAO(new ConnectionFactory().getConnection());
    private QuestDAO questDAO = new DbQuestDAO(new ConnectionFactory().getConnection());
    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
    private View view = new View();
    private Codecooler codecooler = null;
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }

        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")){
            handlePost(httpExchange);
        } else {
            handleGet(httpExchange.getRequestURI().getPath());
        }

        byte[] responseBytes = getResponse().getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handleGet (String URI) {
        String[] URIparts = URI.split("/");
        int codecoolerId = Integer.valueOf(URIparts[URIparts.length - 1]);
        codecooler = codecoolerDAO.getCodecoolerById(codecoolerId);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = new Utils().parseFormData(httpExchange);

        if (inputs.containsKey("add")) {

            int questId = Integer.valueOf(inputs.get("addQuest"));
            Quest doneQuest = questDAO.getQuestById(questId);
            questDAO.addDoneQuestByCodecooler(doneQuest, codecooler);
            codecooler.setExp(codecooler.getExp() + doneQuest.getValue());
            codecooler.setBalance(codecooler.getBalance() + doneQuest.getValue());
            codecoolerDAO.updateCodecooler(codecooler);
        }
        else if (inputs.containsKey("use")) {
            int artifactId = Integer.valueOf(inputs.get("usedArtifact"));
                Artifact usedArtifact = artifactDAO.getArtifactById(artifactId);
                artifactDAO.removeUsedArtifactByCodecooler(usedArtifact, codecooler);
        }
        else {
            codecooler.getBasicUserData().setFirstName(inputs.get("firstName"));
            codecooler.getBasicUserData().setLastName(inputs.get("lastName"));
            codecooler.getBasicUserData().setEmail(inputs.get("email"));
            codecooler.setClassName(inputs.get("className"));
            codecoolerDAO.updateCodecooler(codecooler);
        }

    }

    private String getResponse() {
        String className = codecooler.getClassName();
        List<String> classes = classDAO.getClassesNames();
        Set<Quest> quests = questDAO.getAllQuests();
        Set<Artifact> artifacts = artifactDAO.getBoughtArtifactsByCodecooler(codecooler);

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecooler_editor.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("firstName", codecooler.getBasicUserData().getFirstName());
        model.with("lastName", codecooler.getBasicUserData().getLastName());
        model.with("email", codecooler.getBasicUserData().getEmail());
        model.with("className", className);
        model.with("classes", classes);
        model.with("quests", quests);
        model.with("artifacts", artifacts);



        return template.render(model);
    }

}
