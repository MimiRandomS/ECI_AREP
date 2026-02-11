
package com.mimirandoms.arep1;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

public class HttpServer {
    private static Map<String, Method> endPoints = new HashMap<>();
    private static String basePath = "./";
    private static int port;

    private static void loadComponents() {
        File baseDir = new File("target/classes");
        List<File> classFiles = findFileClasses(baseDir);

        for (File fileClassName : classFiles) {
            try {
                String className = toClassName(fileClassName, baseDir);
                Class<?> c = Class.forName(className);
                if (c.isAnnotationPresent(RestController.class)) {
                    for (Method m : c.getDeclaredMethods()) {
                        if (m.isAnnotationPresent(GetMapping.class)) {
                            String mapping = m.getAnnotation(GetMapping.class).value();
                            endPoints.put(mapping, m);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public static void start(String[] args) throws IOException {
        // port = serverPort;
        port = 35000;

        loadComponents();


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en puerto " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error al aceptar conexión: " + e.getMessage());
                }
            }
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
                Method s = endPoints.get(req.getPath());
                Class<?> controllerClass = s.getDeclaringClass();
                Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

                Object result;
                Class<?>[] paramTypes = s.getParameterTypes();

                if (paramTypes.length == 0) result = s.invoke(controllerInstance);
                else {
                    Object[] argsToPass = new Object[paramTypes.length];
                    for (int i = 0; i < paramTypes.length; i++) {
                        Annotation[] paramAnnotations = s.getParameterAnnotations()[i];
                        for (Annotation annotation : paramAnnotations) {
                            if (annotation instanceof RequestParam) {
                                RequestParam rp = (RequestParam) annotation;
                                String paramName = rp.value();
                                String value = req.getValues(paramName);

                                if (value == null) {
                                    value = rp.defaultValue();
                                }

                                argsToPass[i] = value;
                            }
                        }
                    }

                    result = s.invoke(controllerInstance, argsToPass);
                }

                String body = (result != null) ? result.toString() : "";
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


    private static List<File> findFileClasses(File baseDir) {
        List<File> classes = new ArrayList<>();
        if (baseDir.exists()) {
            File[] files = baseDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) classes.addAll(findFileClasses(file));
                    else if (file.isFile() && file.getName().endsWith(".class")) classes.add(file);
                }
            }
        }

        return classes;
    }

    private static String toClassName(File file, File baseDir) {
        String fullPath = file.getAbsolutePath();
        String basePath = baseDir.getAbsolutePath();
        String relativePath = fullPath.substring(basePath.length() + 1);
        String className = relativePath
                .replace(File.separatorChar, '.')
                .replace(".class", "");

        return className;
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
