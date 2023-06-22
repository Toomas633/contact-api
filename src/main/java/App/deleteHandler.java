package App;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class deleteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            sendResponse(exchange, 200, "");
            return;
        }
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        JSONObject json = new JSONObject(requestBody);
        Integer id = 0;
        try {
            id = Integer.parseInt(json.getString("id"));
        } catch (Exception e) {
            // System.out.println(e.toString());
        }
        String url = DB.getUrl();
        String username = DB.getUsername();
        String password = DB.getPassword();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String insertQuery = "DELETE FROM kontaktid WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                sendResponse(exchange, 500, e.toString());
            }
            connection.close();
            sendResponse(exchange, 200, "Andmebaas muudetud");
        } catch (SQLException e) {
            sendResponse(exchange, 500, e.toString());
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Access-Control-Allow-Methods", "POST");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
}