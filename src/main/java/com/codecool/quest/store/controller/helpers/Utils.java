package com.codecool.quest.store.controller.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public Map<String, String> parseFormData(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            if (keyValue.length == 1) {
                map.put(keyValue[0], "");
            } else {
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                map.put(keyValue[0], value);
            }
        }
        return map;
    }

    public int getIdFromURI (HttpExchange httpExchange) {
        String[] URIparts = httpExchange.getRequestURI().getPath().split("/");
        return Integer.valueOf(URIparts[URIparts.length - 1]);
    }
}
