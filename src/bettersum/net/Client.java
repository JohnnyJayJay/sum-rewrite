package bettersum.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements Runnable {

    private final String host;
    private final int port;
    private final ExecutorService executorService;
    private final AtomicBoolean running;
    private ClientEventAdpater clientEventAdpater;
    private PrintWriter printWriter;
    private Socket socket;
    private boolean closedByServer;

    public Client(String ip, int port) {
        this.host = ip;
        this.port = port;
        this.running = new AtomicBoolean();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void connect() throws IOException {
        if(running.get())
            throw new IllegalStateException("Client is already running");
        running.set(true);
        socket = new Socket(host, port);
        printWriter = new PrintWriter(socket.getOutputStream(), true);

        this.executorService.execute(this);
    }

    public void send(String message) {
        if(printWriter == null)
            throw new IllegalStateException("Client was not started yet.");
        printWriter.println(message + "\n");
    }

    @Override
    public void run() {
        if(clientEventAdpater != null) clientEventAdpater.onConnected();
        while (running.get() && socket.isConnected() && !socket.isClosed()) {
            StringBuilder msg = new StringBuilder();
            try (Scanner scanner = new Scanner(socket.getInputStream())) {
                while (scanner.hasNextLine())
                    msg.append(scanner.nextLine());
                if(clientEventAdpater != null) clientEventAdpater.onMessageReceived(msg.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!closedByServer)
            if(clientEventAdpater != null) clientEventAdpater.onConnectionLost();
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        running.set(false);
        closedByServer = true;
        printWriter.close();
        socket.close();
        if(clientEventAdpater != null) clientEventAdpater.onDisconnected();
    }

    public void setEventAdapter(ClientEventAdpater clientEventAdpater) {
        this.clientEventAdpater = clientEventAdpater;
    }
}
