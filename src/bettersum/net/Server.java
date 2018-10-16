package bettersum.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A server represents a more comfortable ServerSocket with a lot of additional features.
 *
 * @author ForYaSee
 */
public class Server implements Runnable {

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final List<ServerConnection> serverConnections;
    private boolean running;

    //Callbacks
    private Runnable onStarted;
    private Runnable onClosed;
    private Consumer<ServerConnection> onClientConnected;
    private BiConsumer<String, Integer> onClientConnectionLost;
    private BiConsumer<String, Integer> onClientDisconnected;
    private BiConsumer<ServerConnection, String> onClientMessageReceived;

    public Server(int port) throws IOException {
        this.serverConnections = new LinkedList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.serverSocket = new ServerSocket(port);
    }

    public synchronized void start() {
        if (running)
            throw new IllegalStateException("The server is already running and cannot be started twice.");
        running = true;
        executorService.execute(this);
    }

    public synchronized void stop() throws Exception {
        if (!running)
            throw new IllegalStateException("The server is already stopped.");
        running = false;
        serverSocket.close();
        executorService.shutdown();
        onClosed.run();
    }

    @Override
    public void run() {
        onStarted.run();
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(this, socket, (c, s) -> onClientMessageReceived.accept(c, s), c -> onClientConnectionLost.accept(c.getIPAddress(), c.getPort()), c -> {
                    serverConnections.remove(c);
                    onClientDisconnected.accept(c.getIPAddress(), c.getPort());
                });
                serverConnections.add(serverConnection);
                executorService.execute(serverConnection);
                onClientConnected.accept(serverConnection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTo(String ip, int port, String message) {
        getConnectionByIpAndPort(ip, port).send(message);
    }

    public void broadcastToIp(String ip, String message) {
        getConnectionsByIp(ip).forEach(serverConnection -> serverConnection.send(message));
    }

    public void broadcast(String message) {
        serverConnections.forEach(serverConnection -> serverConnection.send(message));
    }

    public void closeConnection(String ip) throws IOException {
        List<ServerConnection> connections = getConnectionsByIp(ip);
        if (connections.size() != 0) {
            for (ServerConnection serverConnection : connections) {
                serverConnection.close();
            }
        }
    }

    public List<ServerConnection> getConnectionsByIp(String ip) {
        return serverConnections.stream().filter(serverConnection -> serverConnection.getIPAddress().equals(ip)).collect(Collectors.toList());
    }

    public ServerConnection getConnectionByIpAndPort(String ip, int port) {
        List<ServerConnection> res = serverConnections.stream().filter(serverConnection -> serverConnection.getIPAddress().equals(ip) && serverConnection.getPort() == port).collect(Collectors.toList());
        if (res.size() == 0)
            throw new NoSuchElementException("There is mo client with that ip and port.");
        return res.get(0);
    }

    public void setOnStarted(Runnable onStarted) {
        this.onStarted = onStarted;
    }

    public void setOnClosed(Runnable onClosed) {
        this.onClosed = onClosed;
    }

    public void setOnClientConnected(Consumer<ServerConnection> onClientConnected) {
        this.onClientConnected = onClientConnected;
    }

    public void setOnClientConnectionLost(BiConsumer<String, Integer> onClientConnectionLost) {
        this.onClientConnectionLost = onClientConnectionLost;
    }

    public void setOnClientDisconnected(BiConsumer<String, Integer> onClientDisconnected) {
        this.onClientDisconnected = onClientDisconnected;
    }

    public void setOnClientMessageReceived(BiConsumer<ServerConnection, String> onClientMessageReceived) {
        this.onClientMessageReceived = onClientMessageReceived;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public List<ServerConnection> getServerConnections() {
        return serverConnections;
    }

    public int getConnectionCount() {
        return serverConnections.size();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public String getIPAddress() {
        return serverSocket.getInetAddress().getHostAddress();
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public String toString() {
        return "Server{" +
                "ip=" + serverSocket.getInetAddress().getHostAddress() +
                "port=" + serverSocket.getLocalPort() +
                "serverConnections=" + serverConnections +
                ", running=" + running +
                '}';
    }
}
