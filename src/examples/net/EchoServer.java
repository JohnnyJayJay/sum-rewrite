package examples.net;

import bettersum.net.Server;
import bettersum.net.ServerConnection;
import bettersum.net.ServerEventAdapter;

/**
 * This Example represents a simple echo server.
 */
public class EchoServer {

    public static void main(String[] args) {
        try {
            Server server = new Server(1234);
            server.setEventAdapter(new TestServerEventAdapter(server));
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class TestServerEventAdapter extends ServerEventAdapter {

        final Server server;

        public TestServerEventAdapter(Server server) {
            this.server = server;
        }

        @Override
        public void onStarted() {
            System.out.println("Server started successfully.");
        }

        @Override
        public void onClosed() {
            System.out.println("Stopped server.");
        }

        @Override
        public void onClientConnected(ServerConnection serverConnection) {
            System.out.printf("Client Connected: %s:%d\n", serverConnection.getIPAddress(), serverConnection.getPort());
        }

        @Override
        public void onClientDisconnected(String ip, int port) {
            System.out.printf("Client disconnected: %s:%d\n", ip, port);
        }

        @Override
        public void onClientConnectionLost(String ip, int port) {
            System.out.printf("Client lost connection: %s:%d\n", ip, port);
        }

        @Override
        public void onClientMessageReceived(ServerConnection serverConnection, String message) {
            System.out.printf("[%s:%d]: %s\n", serverConnection.getIPAddress(), serverConnection.getPort(), message);
            serverConnection.send(message); //Echos message
        }
    }
}
