package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public HttpServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                handleRequest(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static void main(String[] args) throws IOException {
        new HttpServer(8080);
    }

    private static void handleRequest(Socket socket) throws IOException {
        String responseCode = "200";

        String requestLine = HttpClient.readLine(socket);
        System.out.println(requestLine);
        String requestTarget = requestLine.split(" ")[1];
        int questionPos = requestTarget.indexOf('?');
        if (questionPos != -1) {
            String queryString = requestTarget.substring(questionPos + 1);
            int equalsPos = queryString.indexOf('=');
            String parameterValue = queryString.substring(equalsPos + 1);
            responseCode = parameterValue;
        }

        String response = "HTTP/1.1 " + responseCode + " OK\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "Content-Length: 11\r\n" +
                "\r\n" +
                "Hello World";

        socket.getOutputStream().write(response.getBytes());
    }

}
