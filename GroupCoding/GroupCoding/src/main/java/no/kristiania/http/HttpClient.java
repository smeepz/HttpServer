package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private int responseCode = 200;
    private Map<String, String> responseHeaders = new HashMap<>();
    private String responseBody = "";
    private int statusCode;

    public HttpClient(final String host, int port, final String requestTarget) throws IOException {
        String request = "GET " + requestTarget + " HTTP/1.1\r\n"
                        + "Host: " + host + "\r\n"
                        + "\r\n";

        Socket socket = new Socket(host, port);

        socket.getOutputStream().write(request.getBytes());

        String responseLine = readLine(socket);
        System.out.println(responseLine);
        String[] responseLineParts = responseLine.split(" ");
        responseCode = Integer.parseInt(responseLineParts[1]);
        statusCode = Integer.parseInt(responseLineParts[1]);
        String headerLine;

        while(!(headerLine = readLine(socket)).isEmpty()){
            int colonPos = headerLine.indexOf(':');
            String headerName = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos + 1).trim();
            responseHeaders.put(headerName, headerValue);
        }
        int contentLength = Integer.parseInt(getResponseHeader("Content-Length"));
        for (int i = 0; i < contentLength; i++) {
            responseBody+=(char)socket.getInputStream().read();
        }
    }

    public static String readLine(Socket socket) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while((c = socket.getInputStream().read()) != -1) {
            if(c == '\r') {
                socket.getInputStream().read();
                break;
            }
            sb.append((char) c);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
    new HttpClient("urlecho.appspot.com", 80, "/echo?body=Hello+World");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseHeader(String headerName) {
        return responseHeaders.get(headerName);
    }

    public String getResponseBody() {
        return responseBody;
    }
}
