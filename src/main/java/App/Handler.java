package App;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestUri = exchange.getRequestURI().toString();
        if (requestUri.contains("/get/") && exchange.getRequestMethod().equals("GET")) {
            GetContact(exchange);
        } else if (requestUri.contains("/add/") && exchange.getRequestMethod().equals("POST")) {
            AddContact(exchange);
        } else if (requestUri.contains("/search/") && exchange.getRequestMethod().equals("GET")) {
            Search(exchange);
        } else {
            sendResponse(exchange, 500, "Vale url");
        }
    }

    private void GetContact(HttpExchange exchange) throws IOException {
        String userId = exchange.getRequestURI().getPath().split("/api/contact/")[1];
        String url = DB.getUrl();
        String username = DB.getUsername();
        String password = DB.getPassword();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectQuery = "SELECT * FROM kontaktid WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setInt(1, Integer.parseInt(userId));
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String nimi = resultSet.getString("nimi");
                        String salajane = resultSet.getString("salajane");
                        String tel = resultSet.getString("tel");
                        String response = toJSON(id, nimi, salajane, tel).toString();
                        sendResponse(exchange, 200, response);
                    } else {
                        sendResponse(exchange, 500,
                                "Sellise ID-ga kasutajat ei leitud: " + Integer.parseInt(userId));
                    }
                }
            }
            connection.close();
        } catch (SQLException e) {
            sendResponse(exchange, 500, e.toString());
        }
    }

    private void AddContact(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        String params = requestBody.substring(requestBody.indexOf('{') + 1, requestBody.indexOf('}'));
        String[] keyValuePairs = params.split(",");
        Integer id = 0;
        String nimi = "", salajane = "", tel = "";
        for (String pair : keyValuePairs) {
            String[] parts = pair.trim().split(":");
            String key = parts[0].trim();
            String value = parts[1].trim();
            if (key.equals("id")) {
                try {
                    id = Integer.parseInt(value);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            } else if (key.equals("nimi")) {
                nimi = value;
            } else if (key.equals("salajane")) {
                salajane = value;
            } else if (key.equals("tel")) {
                tel = value;
            }
        }
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
                        response.put(toJSON(id, nimi, salajane, tel));
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

    private JSONObject toJSON(int id, String nimi, String salajane, String tel) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nimi", nimi);
        json.put("salajane", salajane);
        json.put("tel", tel);
        return json;
    }
}