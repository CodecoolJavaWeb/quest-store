package com.codecool.quest.store;

import com.codecool.quest.store.controller.*;
import com.codecool.quest.store.controller.admin.*;
import com.codecool.quest.store.controller.codecooler.*;
import com.codecool.quest.store.controller.dao.ConnectionFactory;
import com.codecool.quest.store.controller.dao.DAOFactory;
import com.codecool.quest.store.controller.mentor.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class App {

    public static void main(String[] args) throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        DAOFactory daoFactory = new DAOFactory(connection);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
        httpServer.createContext("/", new Login(daoFactory));
        httpServer.createContext("/logout", new Logout(daoFactory));
        httpServer.createContext("/mentors_manager", new MentorsManager(daoFactory));
        httpServer.createContext("/mentor_editor", new MentorEditor(daoFactory));
        httpServer.createContext("/add_new_mentor", new AddMentor(daoFactory));
        httpServer.createContext("/exp_level_manager", new ExpLevelManager(daoFactory));
        httpServer.createContext("/classes_manager", new ClassesManager(daoFactory));
        httpServer.createContext("/codecoolers_manager", new CodecoolersManager(daoFactory));
        httpServer.createContext("/codecooler_editor", new CodecoolerEditor(daoFactory));
        httpServer.createContext("/add_new_codecooler", new AddCodecooler(daoFactory));
        httpServer.createContext("/quests_manager", new QuestsManager(daoFactory));
        httpServer.createContext("/add_new_quest", new AddQuest(daoFactory));
        httpServer.createContext("/quest_editor", new QuestEditor(daoFactory));
        httpServer.createContext("/artifacts_manager", new ArtifactsManager(daoFactory));
        httpServer.createContext("/add_new_artifact", new AddArtifact(daoFactory));
        httpServer.createContext("/artifact_editor", new ArtifactEditor(daoFactory));
        httpServer.createContext("/codecooler_home", new CodecoolerHome(daoFactory));
        httpServer.createContext("/codecooler_quests", new CodecoolerQuests(daoFactory));
        httpServer.createContext("/codecooler_artifacts", new CodecoolerArtifacts(daoFactory));
        httpServer.createContext("/teams", new Teams(daoFactory));
        httpServer.createContext("/static", new Static());
        httpServer.createContext("/quest_detail", new QuestDetail(daoFactory));
        httpServer.createContext("/buy_artifact", new BuyArtifact(daoFactory));
        httpServer.setExecutor(null);
        httpServer.start();
    }

}
