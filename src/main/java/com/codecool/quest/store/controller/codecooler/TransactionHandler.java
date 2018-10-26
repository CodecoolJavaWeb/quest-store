package com.codecool.quest.store.controller.codecooler;

import com.codecool.quest.store.controller.dao.*;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.controller.helpers.Utils;
import com.codecool.quest.store.model.Artifact;
import com.codecool.quest.store.model.Codecooler;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashSet;
import java.util.Set;

public class TransactionHandler {

    private CodecoolerDAO codecoolerDAO;
    private ArtifactDAO artifactDAO;
    private Codecooler codecooler;
    private Artifact artifact;
    private Set<Codecooler> codecoolersInTeam;
    private int price;
    private boolean tooExpensive;

    TransactionHandler(HttpExchange httpExchange, SessionCookieHandler sessionCookieHandler, DAOFactory daoFactory) {

        this.codecoolerDAO = daoFactory.getCodecoolerDAO();
        this.artifactDAO = daoFactory.getArtifactDAO();

        int artifactId = new Utils().getIdFromURI(httpExchange);
        this.artifact = artifactDAO.getArtifactById(artifactId);

        int basicDataId = sessionCookieHandler.getSession(httpExchange).getBasicDataId();
        this.codecooler = codecoolerDAO.getCodecoolerByBasicDataId(basicDataId);

        setCodecoolersInTeam();

        this.price = getPricePerTeamMember();
        this.tooExpensive = isTooExpensiveForTeam();
    }

    private void setCodecoolersInTeam() {
        if (this.artifact.isMagic() && this.codecooler.isInTeam()) {
            String teamName = this.codecooler.getTeamName();
            this.codecoolersInTeam = codecoolerDAO.getCodecoolerByTeamName(teamName);
        } else {
            this.codecoolersInTeam = new HashSet<>();
            this.codecoolersInTeam.add(this.codecooler);
        }
    }

    private int getPricePerTeamMember() {
        double artifactPrice = this.artifact.getPrice();
        int teamSize = this.codecoolersInTeam.size();
        return (int) Math.ceil(artifactPrice / teamSize);
    }

    private boolean isTooExpensiveForTeam() {
        for (Codecooler codecooler: this.codecoolersInTeam) {
            if (codecooler.getBalance() < this.price)
                return true;
        }
        return false;
    }

    void handleTransaction() {
        if (!this.tooExpensive) {
            for (Codecooler codecooler: this.codecoolersInTeam) {
                addArtifactToBought(codecooler);
            }
        }
    }

    private void addArtifactToBought(Codecooler codecooler) {
        artifactDAO.addArtifactToBought(this.artifact, codecooler);
        int balance = codecooler.getBalance();
        codecooler.setBalance(balance - this.price);
        codecoolerDAO.updateCodecooler(codecooler);
    }

    Codecooler getCodecooler() {
        return codecooler;
    }

    Artifact getArtifact() {
        return artifact;
    }

    int getPrice() {
        return price;
    }

    boolean isTooExpensive() {
        return tooExpensive;
    }
}
