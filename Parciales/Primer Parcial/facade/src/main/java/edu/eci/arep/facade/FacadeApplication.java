package edu.eci.arep.facade;


import java.net.*;
import java.io.*;
import java.nio.file.Files;


public class FacadeApplication {
    static ServerSocket serverSocket;
    static Socket clientSocket;
    static OutputStream out;
    static BufferedReader in;
    static boolean running = true;
    static final String USER_AGENT = "Mozilla/5.0";
    static final String REQUEST_BACKEND_URL = "http://localhost:36000";


    public static void main(String[] args) throws IOException, URISyntaxException {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(46000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 46000.");
            System.exit(1);
        }

        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            handleRequests();
        }
    }

    private static void handleRequests() throws IOException {
        try {
            out = clientSocket.getOutputStream();
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String inputLine, httpRequest = null;
        boolean firstLine = true;
        while ((inputLine = in.readLine()) != null) {
            if (firstLine) {
                httpRequest = inputLine;
                firstLine = false;
            }
            System.out.println("Recibí: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }

        if (httpRequest == null) return;

        String method = httpRequest.split(" ")[0];
        URI requestUri = null;
        try {
            requestUri = new URI(httpRequest.split(" ")[1]);
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        }
        String request = requestUri.getPath();

        if (request.equals("/")) request = "/index.html";
        System.out.println(request.startsWith("/setkv"));
        System.out.println(request.startsWith("/getkv"));
        if (!request.startsWith("/setkv") && !request.startsWith("/getkv")) {
            serveStaticFile(request);
        } else {
            callBackend(method, requestUri.toString());
        }
    }

    private static void callBackend(String method, String request) throws IOException {
        String json = "{}";
        String urlRequestEndpoint = REQUEST_BACKEND_URL + request;
        URL obj = new URL(urlRequestEndpoint);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            json = response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");

        byte[] jsonBytes = json.getBytes();
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write("Content-Type: application/json\r\n".getBytes());
        out.write(("Content-Length: " + jsonBytes.length + "\r\n").getBytes());
        out.write("Connection: close\r\n".getBytes());
        out.write("\r\n".getBytes());
        System.out.println("JSON generado: " + json);
        out.write(jsonBytes);
        out.flush();
    }

    private static void serveStaticFile(String path) {
        String basePath = "page";
        File file = new File(basePath + path);

        try {
            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath());

                String contentType = "text/html";
                String headers =
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + contentType + "\r\n" +
                                "Content-Length: " + fileBytes.length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n";

                OutputStream output = clientSocket.getOutputStream();
                output.write(headers.getBytes());
                output.write(fileBytes);
                output.flush();
            } else {
                String notFound =
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Type: text/html\r\n" +
                                "Content-Length: 22\r\n" +
                                "Connection: close\r\n" +
                                "\r\n" +
                                "<h1>404 Not Found</h1>";
                clientSocket.getOutputStream().write(notFound.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
