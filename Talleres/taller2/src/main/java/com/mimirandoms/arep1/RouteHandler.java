package com.mimirandoms.arep1;

@FunctionalInterface
public interface RouteHandler {
    String handle(Request req, Response res);
}
