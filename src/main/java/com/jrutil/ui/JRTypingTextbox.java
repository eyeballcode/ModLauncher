package com.jrutil.ui;

import com.jrutil.ui.event.TypingEvent;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link javax.swing.JTextField} that allows you to get a {@link com.jrutil.ui.event.TypingEvent} when the user finishes typing in the text field.
 * <br><br>
 * Code example:
 * <p/>
 * <pre>
 *      JRTypingTextbox typingTextbox = new JRTypingTextbox();
 *      typingTextbox.addTypingListener(new TypingListener() {
 *           \@Override
 *           public void typingFinished(TypingEvent event) {
 *               // Prints the contents of the textbox to the console.
 *               System.out.println("User finished typing! Textbox contents: \"" + typingTextbox.getText() + "\"");
 *           }
 *       });
 * </pre>
 * <p/>
 * Note that this typing listener ignores {@linkplain KeyEvent#VK_CONTROL}, {@linkplain KeyEvent#VK_META}, {@linkplain KeyEvent#VK_ALT} and {@linkplain KeyEvent#VK_ESCAPE}.
 * <p/>
 * The constructors {@link #JRTypingTextbox()}, {@link #JRTypingTextbox(String)}, {@link #JRTypingTextbox(int)}, {@link #JRTypingTextbox(String, int)} and {@link #JRTypingTextbox(Document, String, int)}
 * are all the same as those in {@link JTextField}
 *
 * @see KeyUtil#isPrintableKey(KeyEvent)
 */
public class JRTypingTextbox extends JTextField {

    transient List<TypingListener> TypingListeners = new LinkedList<TypingListener>();
    int typingFinishedTime = 500;

    private JRTypingTextboxThread thread = new JRTypingTextboxThread(TypingListeners, typingFinishedTime);

    public JRTypingTextbox() {
        this(null, null, 0);
    }

    public JRTypingTextbox(String text) {
        this(null, text, 0);
    }

    public JRTypingTextbox(int columns) {
        this(null, null, columns);
    }

    public JRTypingTextbox(String text, int columns) {
        this(null, text, columns);
    }

    public JRTypingTextbox(Document document, final String text, int columns) {
        super(document, text, columns);

        addKeyListener(new KeyAdapter() {
            int typeCount = 0;

            @Override
            public void keyReleased(KeyEvent e) {
                if (KeyUtil.isPrintableKey(e) || e.getKeyCode() == 8) {
                    thread.interrupt();
                    thread = new JRTypingTextboxThread(TypingListeners, typingFinishedTime);
                    thread.start();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyUtil.isPrintableKey(e) || e.getKeyCode() == 8) {
                    thread.interrupt();
                    thread = new JRTypingTextboxThread(TypingListeners, typingFinishedTime);
                }
            }
        });
    }

    /**
     * Gets the timeout to trigger the {@link com.jrutil.ui.event.TypingEvent}
     *
     * @return The timeout to trigger the event, in milliseconds. If {@link #setTypingFinishedTime(int)} was not called earlier, the value defaults to 500 milliseconds (Half a second)
     */
    public int getTypingFinishedTime() {
        return typingFinishedTime;
    }

    /**
     * Sets the timeout to trigger the {@link com.jrutil.ui.event.TypingEvent}.
     *
     * @param typingFinishedTime The timeout to trigger the event, in milliseconds
     */
    public void setTypingFinishedTime(int typingFinishedTime) {
        this.typingFinishedTime = typingFinishedTime;
    }

    /**
     * Adds a {@link TypingListener} to run when user typing is complete.
     *
     * @param listener The {@link TypingListener} to attach.
     * @see TypingListener
     * @see com.jrutil.ui.event.TypingEvent
     * @see #removeTypingListner(TypingListener)
     * @see #getTypingListners()
     */
    public synchronized void addTypingListener(TypingListener listener) {
        if (listener == null) return;
        TypingListeners.add(listener);
    }

    /**
     * Removes a {@link TypingListener} from the list of {@link TypingListener}s to run when user typing is complete.
     *
     * @param listener The {@link TypingListener} to remove.
     * @see TypingListener
     * @see com.jrutil.ui.event.TypingEvent
     * @see #addTypingListener(TypingListener)
     * @see #getTypingListners()
     */
    public synchronized void removeTypingListner(TypingListener listener) {
        if (listener == null) return;
        if (TypingListeners.size() == 1) return;
        TypingListeners.remove(listener);
    }

    /**
     * Gets an array {@link TypingListener} that will be run when the user finishes typing.
     *
     * @return The array of {@link TypingListener}
     * @see TypingListener
     * @see com.jrutil.ui.event.TypingEvent
     * @see #addTypingListener(TypingListener)
     * @see #removeTypingListner(TypingListener)
     */
    public TypingListener[] getTypingListners() {
        return TypingListeners.toArray(new TypingListener[TypingListeners.size()]);
    }


}
