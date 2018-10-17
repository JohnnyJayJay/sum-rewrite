package examples.net;

import bettersum.net.Client;
import bettersum.net.ClientEventAdpater;

import java.io.IOException;

public class EchoClient {

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 1234);
            client.setEventAdapter(new TestClientEventAdapter(client));
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TestClientEventAdapter extends ClientEventAdpater {

        final Client client;

        TestClientEventAdapter(Client client) {
            this.client = client;
        }

        @Override
        public void onConnected() {
            System.out.println("Connected to Server.");
            client.send("Client");
        }

        @Override
        public void onDisconnected() {
            System.out.println("Disconnected from Server.");
        }

        @Override
        public void onConnectionLost() {
            System.out.println("Connection lost.");
        }

        @Override
        public void onMessageReceived(String message) {
            System.out.printf("Received Message: %s\n", message);
        }
    }
}
