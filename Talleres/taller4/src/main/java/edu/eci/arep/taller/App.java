package edu.eci.arep.taller;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer.staticfiles("page");

        HttpServer.get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        HttpServer.get("/pi", (req, res) -> String.valueOf(Math.PI));

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "5000"));
        HttpServer.start(port);

    }
}
