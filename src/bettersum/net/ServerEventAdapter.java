package bettersum.net;

public class ServerEventAdapter {

    public void onStarted() {}

    public void onClosed() {}

    public void onClientConnected(ServerConnection serverConnection) {}

    public void onClientDisconnected(String ip, int port) {}

    public void onClientConnectionLost(String ip, int port) {}

    public void onClientMessageReceived(ServerConnection serverConnection, String message) {}
}
