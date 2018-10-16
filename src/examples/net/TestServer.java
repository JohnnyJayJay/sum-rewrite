package examples.net;

import bettersum.net.Server;
import bettersum.net.ServerConnection;

/**
 * This Example represents a simple echo server.
 */
public class TestServer {

    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(1234);
            server.setOnStarted(() -> System.out.println(String.format("Server started successfully. IP: %s", server.getIPAddress())));
            server.setOnClosed(() -> System.out.println("Server was closed."));
            server.setOnClientConnected(client -> System.out.println(String.format("New client connected: %s", client.getIPAddress())));
            server.setOnClientConnectionLost((ip, port) -> System.out.println(String.format("Client with the ip `%s:%d` lost the connection.", ip, port)));
            server.setOnClientDisconnected((ip, port) -> System.out.print(String.format("Client with the ip `%s:%d` disconnected from the server.", ip, port)));
            server.setOnClientMessageReceived(ServerConnection::send); //Echos the message that was sent to the server
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
