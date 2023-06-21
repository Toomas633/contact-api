package App;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SearchHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestUri = exchange.getRequestURI().toString();
        if (exchange.getRequestMethod().equals("POST")) {
            if (requestUri.contains("/?search=")) {
                Search(exchange);
            }
        } else {
            String response = "";
            if (requestUri.contains("/?contact=")) {
                try {
                    InputStream fileInputStream = getClass().getResourceAsStream("/static/contact.html");
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    fileInputStream.close();
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, buffer.length);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(buffer);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    response = "Internal Server Error";
                    exchange.sendResponseHeaders(500, response.length());
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.close();
                }
            } else {
                try {
                    InputStream fileInputStream = getClass().getResourceAsStream("/static/search.html");
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    fileInputStream.close();
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, buffer.length);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(buffer);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    response = "Internal Server Error";
                    exchange.sendResponseHeaders(500, response.length());
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.close();
                }
            }
        }
    }

    private void Search(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        String input = requestBody.substring(requestBody.indexOf(':') + 1);
        String url = DB.getUrl();
        String username = DB.getUsername();
        String password = DB.getPassword();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectQuery = "SELECT * FROM kontaktid WHERE nimi LIKE ? OR salajane LIKE ? OR tel LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, "%" + input + "%");
                statement.setString(2, "%" + input + "%");
                statement.setString(3, "%" + input + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    JSONArray response = new JSONArray();
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String nimi = resultSet.getString("nimi");
                        String salajane = resultSet.getString("salajane");
                        String tel = resultSet.getString("tel");
                        Contact user = new Contact(id, nimi, salajane, tel);
                        response.put(user.toJSON());
                    }
                    sendResponse(exchange, 200, response.toString());
                }
            }
            connection.close();
        } catch (SQLException e) {
            sendResponse(exchange, 500, e.toString());
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
}
