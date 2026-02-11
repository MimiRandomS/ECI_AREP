package edu.eci.arep.taller;

@FunctionalInterface
public interface RouteHandler {
    String handle(Request req, Response res);
}
