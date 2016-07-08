package com.jrutil;

import java.awt.event.ActionListener;

/**
 * A class representing delayable code.
 */
public class RepeatedCode extends Thread {

    ActionListener l;

    long d;

    private RepeatedCode(ActionListener listener, long delay) {
        l = listener;
        d = delay;
        setDaemon(false);
        start();
    }

    /**
     * Stops a code loop before.
     *
     * @param code The {@link RepeatedCode} to stop.
     */
    public static void clearInterval(RepeatedCode code) {
        code.interrupt();
    }

    /**
     * Delays running of code, just like JavaScript's window#setInterval
     *
     * @param listener The listener when it's time to run the code.
     * @param delay    The amount of time to delay running the code in milliseconds, in the loop.
     * @return A stoppable thread, just like the int returned in JavaScript's window#setInterval
     */
    public static RepeatedCode setInterval(ActionListener listener, long delay) {
        return new RepeatedCode(listener, delay);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(d);
                l.actionPerformed(null);
            } catch (InterruptedException e) {
                // JS's clearInterval called.
                return;
            }
        }
    }

}
