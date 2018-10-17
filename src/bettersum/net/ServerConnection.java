package bettersum.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a connection with an client.
 *
 * @author ForYaSee
 */
public class ServerConnection implements Runnable {

    private final Server server;
    private final Socket socket;
    private final PrintWriter printWriter;
    private boolean serverClose = false;

    //Internal Callbacks
    private BiConsumer<ServerConnection, String> onMessage;
    private Consumer<ServerConnection> onClosed;
    private Consumer<ServerConnection> onConnectionLost;

    ServerConnection(Server server, Socket socket, BiConsumer<ServerConnection, String> onMessage, Consumer<ServerConnection> onClosed, Consumer<ServerConnection> onConnectionLost) throws IOException {
        this.server = server;
        this.socket = socket;
        this.onMessage = onMessage;
        this.onClosed = onClosed;
        this.onConnectionLost = onConnectionLost;
        printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        while (socket.isConnected() && !socket.isClosed()) {
            try (Scanner scanner = new Scanner(socket.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String msg = scanner.nextLine();
                    if (!msg.equals(""))
                        onMessage.accept(this, msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!serverClose)
            onConnectionLost.accept(this);
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        printWriter.println(message + "\n");
    }

    public void close() throws IOException {
        serverClose = true;
        printWriter.close();
        socket.close();
        onClosed.accept(this);
    }

    public String getIPAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return socket.getPort();
    }
}
