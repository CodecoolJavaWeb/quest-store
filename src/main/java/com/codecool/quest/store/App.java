package com.codecool.quest.store;

import com.codecool.quest.store.controller.Logout;
import com.codecool.quest.store.controller.Login;
import com.codecool.quest.store.controller.MentorsManager;
import com.codecool.quest.store.controller.Static;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
        httpServer.createContext("/", new Login());
        httpServer.createContext("/logout", new Logout());
        httpServer.createContext("/mentors_manager", new MentorsManager());
        httpServer.createContext("/static", new Static());
        httpServer.setExecutor(null);
        httpServer.start();
    }
}
