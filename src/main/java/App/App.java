package App;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/api/", new UserHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started: http://localhost:8000");
    }

    static class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("GET")) {
                handleGetRequest(exchange);
            } else if (exchange.getRequestMethod().equals("POST")) {
                handlePostRequest(exchange);
            }
        }

        private void handleGetRequest(HttpExchange exchange) throws IOException {
            String userId = exchange.getRequestURI().getPath().split("/api/")[1];
            String url = dbconfig.getUrl();
            String username = dbconfig.getUsername();
            String password = dbconfig.getPassword();
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
                            User user = new User(id, nimi, salajane, tel);
                            String response = user.toString();
                            sendResponse(exchange, 200, response);
                        } else {
                            sendResponse(exchange, 500, "Sellise ID-ga kasutajat ei leitud: " + Integer.parseInt(userId));
                        }
                    }
                }
                connection.close();
            } catch (SQLException e) {
                sendResponse(exchange, 500, e.toString());
            }
        }

        private void handlePostRequest(HttpExchange exchange) throws IOException {
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            System.out.println(requestBody);
            String params = requestBody.substring(requestBody.indexOf('{') + 1, requestBody.indexOf('}'));
            String[] keyValuePairs = params.split(",");
            Integer id = 0;
            String nimi = "", salajane = "", tel = "";
            for (String pair : keyValuePairs) {
                String[] parts = pair.trim().split("=");
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
            String url = dbconfig.getUrl();
            String username = dbconfig.getUsername();
            String password = dbconfig.getPassword();
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
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

    static class User {
        private int id;
        private String name;
        private String secret;
        private String tel;

        public User(int id, String name, String secret, String tel) {
            this.id = id;
            this.name = name;
            this.secret = secret;
            this.tel = tel;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", nimi=" + name + ", salajane=" + secret + ", tel=" + tel +'}';
        }
    }
}