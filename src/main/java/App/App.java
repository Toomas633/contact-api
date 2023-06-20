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
        server.createContext("/", new UserHandler());
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
            String userId = exchange.getRequestURI().getPath().split("/kasutaja/")[1];
            String url = "jdbc:postgresql://mysql.toomas633.com:5432/smit";
            String username = "postgres";
            String password = "Toomas2001";
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String selectQuery = "SELECT * FROM kasutajad WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                    statement.setInt(1, Integer.parseInt(userId));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            // Retrieve data from the result set
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
            } catch (SQLException e) {
                sendResponse(exchange, 500, e.toString());
            }
        }

        private void handlePostRequest(HttpExchange exchange) throws IOException {
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            System.out.print(requestBody);
            // Logic to parse the request body and create a new user in the database
            sendResponse(exchange, 201, "Kasutaja loodud");
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