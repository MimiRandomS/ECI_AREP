package com.mimirandoms.arep1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private Map<String, String> queryParams = new HashMap<>();
    private String body;

    public Request(ArrayList<String> rawRequest, String rawBody) {
        String[] firstLine = rawRequest.get(0).split(" ");
        method = firstLine[0];

        String fullPath = firstLine[1];

        if (fullPath.contains("?")) {
            String[] pathAndQuery = fullPath.split("\\?");
            path = pathAndQuery[0];
            String queryString = pathAndQuery[1];

            String[] params = queryString.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        } else {
            path = fullPath;
        }

        body = rawBody;
    }

    public String getMethod() {
        return method;
    }
    public String getPath() {
        return path;
    }
    public String getValues(String key) {
        return queryParams.get(key);
    }

    public String getBody() {
        return body;
    }
}
