package edu.eci.arep.taller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private String contentType;
    private int statusCode;
    private byte[] bodyBytes;

    public Response(int statusCode, String contentType) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.bodyBytes = new byte[0];
    }

    // Enviar String
    public void send(String body) {
        this.bodyBytes = body.getBytes(StandardCharsets.UTF_8);
    }

    // Enviar bytes (para imágenes, mp3, etc.)
    public void send(byte[] body) {
        this.bodyBytes = body;
    }

    private String getStatusMessage() {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "";
        };
    }

    public void flush(OutputStream out) throws IOException {
        String header = "HTTP/1.1 " + statusCode + " " + getStatusMessage() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + bodyBytes.length + "\r\n\r\n";
        out.write(header.getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
        out.flush();
    }
}
