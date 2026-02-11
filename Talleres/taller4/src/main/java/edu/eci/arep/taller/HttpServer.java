package edu.eci.arep.taller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static Map<String, RouteHandler> endPoints = new HashMap<>();
    private static String basePath = "./";
    private static int port;
    private static boolean running = false;
    private static ServerSocket serverSocket;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void start(int serverPort) throws IOException {
        port = serverPort;
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("Servidor iniciado en puerto " + port);

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error aceptando conexión: " + e.getMessage());
                }
            }
        }
    }

    public static void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
            System.out.println("Servidor detenido.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outStream = clientSocket.getOutputStream()
        ) {
            ArrayList<String> rawRequest = readRequest(in);
            String rawBody = "";

            Request req = new Request(rawRequest, rawBody);
            Response res = new Response(200, "text/html");

            if (endPoints.containsKey(req.getPath())) {
                RouteHandler handler = endPoints.get(req.getPath());
                String body = handler.handle(req, res);
                res.send(body);
                res.flush(outStream);
            } else {
                String filePath = req.getPath().equals("/") ? "/index.html" : req.getPath();
                serveStaticFile(filePath, outStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> readRequest(BufferedReader in) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            lines.add(line);
        }
        return lines;
    }

    public static void get(String path, RouteHandler handler) {
        endPoints.put(path, handler);
    }

    public static void staticfiles(String path) {
        basePath = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    private static void serveStaticFile(String path, OutputStream outStream) throws IOException {
        File file = new File(basePath + path);
        Response res;

        if (file.exists() && !file.isDirectory()) {
            String contentType = getMimeType(path);

            if (path.endsWith(".html") || path.endsWith(".css") || path.endsWith(".js")) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                res = new Response(200, contentType);
                res.send(sb.toString());

            } else {
                byte[] bytes = Files.readAllBytes(file.toPath());
                res = new Response(200, contentType);
                res.send(bytes);
            }

        } else {
            res = new Response(404, "text/html");
            res.send("<h1>404 - Not Found</h1>");
        }
        res.flush(outStream);
    }

    private static String getMimeType(String path) {
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".jpeg") || path.endsWith(".jpg")) return "image/jpeg";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".mp3")) return "audio/mpeg";
        if (path.endsWith(".mp4")) return "video/mp4";
        return "text/html";
    }
}
