package com.jrutil;

import java.awt.event.ActionListener;

/**
 * A class representing delayable code.
 */
public class DelayedCode extends Thread {

    ActionListener l;

    long d;

    private DelayedCode(ActionListener listener, long delay) {
        l = listener;
        d = delay;
        setDaemon(false);
        start();
    }

    /**
     * Clears a delayed code before it's runned.
     *
     * @param code The {@link DelayedCode} to stop.
     */
    public static void clearTimeout(DelayedCode code) {
        code.interrupt();
    }

    /**
     * Delays running of code, just like JavaScript's window#setTimeout
     *
     * @param listener The listener when it's time to run the code.
     * @param delay    The amount of time to delay before running the code in milliseconds.
     * @return A stoppable thread, just like the int returned in JavaScript's window#setTimeout
     */
    public static DelayedCode setTimeout(ActionListener listener, long delay) {
        return new DelayedCode(listener, delay);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(d);
            l.actionPerformed(null);
        } catch (InterruptedException e) {
            // JS's clearTimeout called.
        }
    }

}
