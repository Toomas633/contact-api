package App;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/add", new addHandler());
        server.createContext("/search", new searchHandler());
        server.createContext("/get", new getHandler());
        server.createContext("/delete", new deleteHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started: http://localhost:8000");
    }
}