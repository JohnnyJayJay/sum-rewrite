package bettersum.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private final AtomicBoolean running;
    private ServerEventAdapter serverEventAdapter;

    public Server(int port) throws IOException {
        this.running = new AtomicBoolean();
        this.serverConnections = new LinkedList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.serverSocket = new ServerSocket(port);
    }

    public synchronized void start() {
        if (running.get())
            throw new IllegalStateException("The server is already running and cannot be started twice.");
        running.set(true);
        executorService.execute(this);
    }

    public synchronized void stop() throws Exception {
        if (!running.get())
            throw new IllegalStateException("The server is already stopped.");
        running.set(true);
        serverSocket.close();
        executorService.shutdown();
        if(serverEventAdapter != null) serverEventAdapter.onClosed();
    }

    @Override
    public void run() {
        if (serverEventAdapter != null) serverEventAdapter.onStarted();
        while (running.get()) {
            try {
                Socket socket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(this, socket, (c, s) -> {
                    if (serverEventAdapter != null) serverEventAdapter.onClientMessageReceived(c, s);
                }, c -> {
                    if (serverEventAdapter != null)
                        serverEventAdapter.onClientConnectionLost(c.getIPAddress(), c.getPort());
                }, c -> {
                    serverConnections.remove(c);
                    if (serverEventAdapter != null)
                        serverEventAdapter.onClientDisconnected(c.getIPAddress(), c.getPort());
                });
                serverConnections.add(serverConnection);
                executorService.execute(serverConnection);
                if (serverEventAdapter != null) serverEventAdapter.onClientConnected(serverConnection);
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

    public void setEventAdapter(ServerEventAdapter serverEventAdapter) {
        this.serverEventAdapter = serverEventAdapter;
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
        return running.get();
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
