package com.codecool.quest.store.controller;

import com.codecool.quest.store.controller.helpers.MimeTypeResolver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

public class Static implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        URI uri = httpExchange.getRequestURI();
        String path = "." + uri.getPath();

        ClassLoader classLoader = getClass().getClassLoader();
        URL fileURL = classLoader.getResource(path);

        if (fileURL == null) {
            send404(httpExchange);
        } else {
            sendFile(httpExchange, fileURL);
        }

    }

    private void send404(HttpExchange httpExchange) throws IOException {
        String response = "404 (Not Found)\n";
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private void sendFile(HttpExchange httpExchange, URL fileURL) throws IOException {
        File file = new File(fileURL.getFile());
        MimeTypeResolver resolver = new MimeTypeResolver(file);
        String mime = resolver.getMimeType();

        httpExchange.getResponseHeaders().set("Content-Type", mime);
        httpExchange.sendResponseHeaders(200, 0);

        OutputStream os = httpExchange.getResponseBody();

        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0) {
            os.write(buffer,0,count);
        }
        os.close();
    }
}
