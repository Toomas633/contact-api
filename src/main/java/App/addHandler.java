package App;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class addHandler implements HttpHandler {
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
            //System.out.println(e.toString());
        }
        String nimi = json.getString("nimi");
        String salajane = json.getString("salajane");
        String tel = json.getString("tel");
        String url = DB.getUrl();
        String username = DB.getUsername();
        String password = DB.getPassword();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String insertQuery = "";
            if (id.equals(0)) {
                insertQuery = "INSERT INTO kontaktid (nimi, salajane, tel) VALUES (?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                    statement.setString(1, nimi);
                    statement.setString(2, salajane);
                    statement.setString(3, tel);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    sendResponse(exchange, 500, e.toString());
                }
            } else {
                insertQuery = "INSERT INTO kontaktid (id, nimi, salajane, tel) VALUES (?, ?, ?, ?) " +
                        "ON CONFLICT (id) DO UPDATE SET nimi = EXCLUDED.nimi, salajane = EXCLUDED.salajane, tel = EXCLUDED.tel";
                try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                    statement.setInt(1, id);
                    statement.setString(2, nimi);
                    statement.setString(3, salajane);
                    statement.setString(4, tel);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    sendResponse(exchange, 500, e.toString());
                }
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