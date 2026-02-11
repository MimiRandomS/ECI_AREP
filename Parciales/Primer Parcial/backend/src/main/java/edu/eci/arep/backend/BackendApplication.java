package edu.eci.arep.backend;


import java.net.*;
import java.io.*;
import java.util.*;

public class BackendApplication {
	static ServerSocket serverSocket;
	static Socket clientSocket;
	static OutputStream out;
	static BufferedReader in;
	static boolean running = true;
	static Repository<String, String> repository = new Repository<>();

	public static void main(String[] args) throws IOException, URISyntaxException {
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(36000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 36000.");
			System.exit(1);
		}

		while (running){
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

	private static void handleRequests() throws IOException, URISyntaxException {
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
			if (firstLine){
				httpRequest = inputLine;
				firstLine = false;
			}
			System.out.println("Recibí: " + inputLine);
			if (!in.ready()) {break; }
		}

		if (httpRequest == null) return;
		handleEndpoints(httpRequest);

	}

	private static void handleEndpoints(String httpRequest) throws IOException {
		String json = "{ }";
		String method = httpRequest.split(" ")[0];
		URI requestUri = null;
		try {
			requestUri = new URI(httpRequest.split(" ")[1]);
		} catch (URISyntaxException e) {
			System.out.println(e.getMessage());
		}
        String request = requestUri.getPath();
		if (method.equals("GET")){
			if (request.startsWith("/setkv")){
				String key = requestUri.getQuery().split("&")[0].split("=")[1];
				String value = requestUri.getQuery().split("&")[1].split("=")[1];
				json = setKV(key, value);
			} else if (request.startsWith("/getkv")) {
				String key = requestUri.getQuery().split("=")[1];
				json = getKV(key);
			}
		}


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

	private static String setKV(String key, String value){
		repository.save(key, value);
		List<String> keys = new ArrayList<>(List.of("key", "value", "status"));
		List<String> values = new ArrayList<>(List.of(key, value, "created"));
		return jsonBuilder(keys, values);
	}

	private static String getKV(String key){
		String value = repository.get(key);
		List<String> keys;
		List<String> values;
		if (value == null){
			keys = new ArrayList<>(List.of("error", "key"));
			values = new ArrayList<>(List.of("key_not_found", key));
		} else {
			keys = new ArrayList<>(List.of("key", "value"));
			values = new ArrayList<>(List.of(key, value));
		}
		return jsonBuilder(keys, values);
	}

	private static String jsonBuilder(List<String> keys, List<?> values) {
		StringBuilder result = new StringBuilder();
		result.append("{");

		for (int i = 0; i < keys.size(); i++) {
			result.append("\"").append(keys.get(i)).append("\": ");

			Object value = values.get(i);
			if (value instanceof List<?> list) {
				result.append("[");
				for (int j = 0; j < list.size(); j++) {
					result.append("\"").append(list.get(j).toString()).append("\"");
					if (j + 1 < list.size()) result.append(", ");
				}
				result.append("]");
			} else {
				result.append("\"").append(value.toString()).append("\"");
			}

			if (i + 1 < keys.size()) result.append(", ");
		}

		result.append("}");
		return result.toString();
	}
}

