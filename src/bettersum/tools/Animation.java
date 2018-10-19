package bettersum.tools;

import java.io.Closeable;
import java.io.Serializable;

public abstract class Animation extends Thread implements Serializable, Closeable {

    private boolean stopped = false;
    private final long interval;

    /**
     * Constructs a new animation with interval 30 milliseconds
     * @see Animation#Animation(long)
     */
    public Animation() {
        this(30L);
    }

    /**
     * Constructs an Animation that got executed every x milliseconds
     * @param interval The interval in milliseconds
     */
    public Animation(long interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        while (!stopped)
            try {
                draw();
                sleep(interval);
            } catch (Exception ignored) {

            }
    }

    /**
     * The methods that gets executed every x milliseconds
     * @throws Exception Whenever the run method throws an exception
     */
    public abstract void draw() throws Exception;

    /**
     * Stops the animation
     */
    @Override
    public void close() {
        stopped = true;
    }
}
