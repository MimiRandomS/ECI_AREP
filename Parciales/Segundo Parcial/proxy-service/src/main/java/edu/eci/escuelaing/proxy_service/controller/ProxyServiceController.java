package edu.eci.escuelaing.proxy_service.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProxyServiceController {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static int intercalation = 2;
    @GetMapping("/collatzsequence")
    public String collatzRequest(@RequestParam int value) {
        try {
            System.out.println("/collatzsequence?value=" + value);
            return request("/collatzsequence?value=" + value);
        } catch (IOException e) {
            return "{\"error\":\"La peticion fallo\"}";
        }
    }

    public String request(String path) throws IOException {
        String url;
        if (path.startsWith("/collatzsequence") && intercalation % 2 == 0) {
            url = "http://52.91.9.72:8080" + path;
            intercalation++;
        } else if (path.startsWith("mathService2") && intercalation % 2 == 1) {
            url = "http://54.226.14.206:8080" + path;
            intercalation++;
        } else {
            return "{\"error\":\"Ruta no válida\"}";
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
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
            return response.toString();
        } else {
            return "{\"error\":\"La peticion fallo\"}";
        }
    }
}






