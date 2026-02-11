package com.mimirandoms.arep1;

import java.io.IOException;

public class app {
    public static void main(String[] args) throws IOException {
        HttpServer.staticfiles("page");

        HttpServer.get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        HttpServer.get("/pi", (req, res) -> String.valueOf(Math.PI));

        HttpServer.start(35000);
    }


}
