package App;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/api/", new ApiHandler());
        server.createContext("/", new SearchHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started: http://localhost:8000");
    }
}