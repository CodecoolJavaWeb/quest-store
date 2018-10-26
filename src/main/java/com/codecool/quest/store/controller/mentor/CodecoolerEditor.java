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

    private CodecoolerDAO codecoolerDAO;
    private ClassDAO classDAO;
    private QuestDAO questDAO;
    private ArtifactDAO artifactDAO ;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    public CodecoolerEditor(DAOFactory daoFactory) {
        this.codecoolerDAO = daoFactory.getCodecoolerDAO();
        this.classDAO = daoFactory.getClassDAO();
        this.questDAO = daoFactory.getQuestDAO();
        this.artifactDAO = daoFactory.getArtifactDAO();
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.MENTOR)) {
            view.redirectToPath(httpExchange, "/");
        }

        String method = httpExchange.getRequestMethod();
        String[] URIparts = httpExchange.getRequestURI().getPath().split("/");
        int codecoolerId = Integer.valueOf(URIparts[URIparts.length - 1]);
        Codecooler codecooler = codecoolerDAO.getCodecoolerById(codecoolerId);

        if (method.equals("POST")){
            handlePost(httpExchange, codecooler);
        }

        byte[] responseBytes = getResponse(codecooler).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }


    private void handlePost(HttpExchange httpExchange, Codecooler codecooler) throws IOException {
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

    private String getResponse(Codecooler codecooler) {
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
