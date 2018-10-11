package com.codecool.quest.store;

import com.codecool.quest.store.controller.*;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
        httpServer.createContext("/", new Login());
        httpServer.createContext("/logout", new Logout());
        httpServer.createContext("/mentors_manager", new MentorsManager());
        httpServer.createContext("/mentor_editor", new MentorEditor());
        httpServer.createContext("/add_new_mentor", new AddMentor());
        httpServer.createContext("/exp_level_manager", new ExpLevelManager());
        httpServer.createContext("/classes_manager", new ClassesManager());
        httpServer.createContext("/codecoolers_manager", new CodecoolersManager());
        httpServer.createContext("/codecooler_editor", new CodecoolerEditor());
        httpServer.createContext("/add_new_codecooler", new AddCodecooler());
        httpServer.createContext("/quests_manager", new QuestsManager());
        httpServer.createContext("/add_new_quest", new AddQuest());
        httpServer.createContext("/quest_editor", new QuestEditor());
        httpServer.createContext("/artifacts_manager", new ArtifactsManager());
        httpServer.createContext("/add_new_artifact", new AddArtifact());
        httpServer.createContext("/artifact_editor", new ArtifactEditor());
        httpServer.createContext("/codecooler_home", new CodecoolerHome());
        httpServer.createContext("/codecooler_quests", new CodecoolerQuests());
        httpServer.createContext("/codecooler_artifacts", new CodecoolerArtifacts());
        httpServer.createContext("/teams", new Teams());
        httpServer.createContext("/static", new Static());
        httpServer.createContext("/quest_detail", new QuestDetail());
        httpServer.createContext("/buy_artifact", new BuyArtifact());
        httpServer.setExecutor(null);
        httpServer.start();
    }

}
