package App;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class searchHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            sendResponse(exchange, 200, "");
            return;
        }
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        JSONObject json = new JSONObject(requestBody);
        String input = json.getString("value");
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

    private JSONObject toJSON(int id, String nimi, String salajane, String tel) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nimi", nimi);
        json.put("salajane", salajane);
        json.put("tel", tel);
        return json;
    }
}