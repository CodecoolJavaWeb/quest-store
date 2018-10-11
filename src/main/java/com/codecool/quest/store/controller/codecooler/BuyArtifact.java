package com.codecool.quest.store.controller.codecooler;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.Set;

public class BuyArtifact implements HttpHandler {

    private CodecoolerDAO codecoolerDAO = new DbCodecoolerDAO(new ConnectionFactory().getConnection());
    private ArtifactDAO artifactDAO = new DbArtifactDAO(new ConnectionFactory().getConnection());
    private Codecooler codecooler = null;
    private Artifact artifact = null;
    private Set<Codecooler> codecoolersInTeam = null;
    private int price = 0;
    private boolean tooExpensive = false;
    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            handlePost(httpExchange);
        }

        byte[] responseBytes = getResponse(httpExchange).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {

        if (artifact.isMagic()) {
            for (Codecooler codecooler: codecoolersInTeam) {
                addArtifactToBought(codecooler);
            }
        } else {
            addArtifactToBought(this.codecooler);
        }

        view.redirectToPath(httpExchange,"/codecooler_artifacts");
    }

    private void addArtifactToBought(Codecooler codecooler) {
        artifactDAO.addArtifactToBought(this.artifact, codecooler);
        int balance = codecooler.getBalance();
        codecooler.setBalance(balance - price);
        codecoolerDAO.updateCodecooler(codecooler);
    }

    private String getResponse(HttpExchange httpExchange) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/buy_artifact.twig");
        JtwigModel model = JtwigModel.newModel();

        int basicDataId = sessionCookieHandler.getSession().getBasicDataId();
        codecooler = codecoolerDAO.getCodecoolerByBasicDataId(basicDataId);
        int artifactId = new Utils().getIdFromURI(httpExchange);
        artifact = artifactDAO.getArtifactById(artifactId);

        if (artifact.isMagic()) {
            handleMagicArtifact();
        } else {
            price = artifact.getPrice();
            tooExpensive = codecooler.getBalance() < price;
        }

        model.with("codecooler", codecooler);
        model.with("artifact", artifact);
        model.with("price", price);
        model.with("tooExpensive", tooExpensive);
        return template.render(model);
    }

    private void handleMagicArtifact() {
        String teamName = codecooler.getTeamName();

        if (teamName.isEmpty()) {
            price = artifact.getPrice();
            tooExpensive = codecooler.getBalance() < price;
        } else {
            codecoolersInTeam = codecoolerDAO.getCodecoolerByTeamName(teamName);
            price = (int)Math.ceil(artifact.getPrice() / (codecoolersInTeam.size() * 1.0));
            tooExpensive = isTooExpensiveForTeam(price);
        }
    }

    private boolean isTooExpensiveForTeam(int price) {
        for (Codecooler codecooler: codecoolersInTeam) {
            if (codecooler.getBalance() < price)
                return true;
        }
        return false;
    }
}
