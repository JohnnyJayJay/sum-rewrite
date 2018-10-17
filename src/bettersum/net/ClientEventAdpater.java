package bettersum.net;

public abstract class ClientEventAdpater {

    public void onConnected() {}

    public void onDisconnected() {}

    public void onConnectionLost() {}

    public void onMessageReceived(String message) {}
}
