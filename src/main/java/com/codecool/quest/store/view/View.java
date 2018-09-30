package com.codecool.quest.store.view;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class View {

    public void sendResponse(HttpExchange httpExchange, byte[] responseBytes) throws IOException {
        httpExchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}
