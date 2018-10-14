package com.codecool.quest.store.controller.codecooler;

import com.codecool.quest.store.controller.helpers.AccountType;
import com.codecool.quest.store.controller.helpers.SessionCookieHandler;
import com.codecool.quest.store.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;

public class BuyArtifact implements HttpHandler {

    private View view = new View();
    private SessionCookieHandler sessionCookieHandler = new SessionCookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!sessionCookieHandler.isSessionValid(httpExchange, AccountType.CODECOOLER)) {
            view.redirectToPath(httpExchange, "/");
        }

        TransactionHandler transactionHandler = new TransactionHandler(httpExchange, sessionCookieHandler);

        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            transactionHandler.handleTransaction();
            view.redirectToPath(httpExchange,"/codecooler_artifacts");
        }

        byte[] responseBytes = getResponse(transactionHandler).getBytes();
        view.sendResponse(httpExchange, responseBytes);
    }

    private String getResponse(TransactionHandler transactionHandler) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/buy_artifact.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("codecooler", transactionHandler.getCodecooler());
        model.with("artifact", transactionHandler.getArtifact());
        model.with("price", transactionHandler.getPrice());
        model.with("tooExpensive", transactionHandler.isTooExpensive());
        return template.render(model);
    }
}
