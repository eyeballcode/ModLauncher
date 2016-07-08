package com.jrutil.ui;

import com.jrutil.ui.event.TypingEvent;

/**
 * A listener that runs {@link #typingFinished(TypingEvent)} when bound to a {@link JRTypingTextbox} and the user finishes typing in it.
 */
public interface TypingListener {

    /**
     * Run when the user finishes typing.
     *
     * @param event A {@link TypingEvent}
     */
    void typingFinished(TypingEvent event);

    /**
     * Run when the user starts typing.
     * @param event A {@link TypingEvent}
     */
    void typingStarted(TypingEvent event);

}
