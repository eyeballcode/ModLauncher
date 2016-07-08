package com.jrutil.ui;

import com.jrutil.ui.event.TypingEvent;

import java.util.List;

class JRTypingTextboxThread extends Thread {

    List<TypingListener> typingFinishedListeners;

    int typingFinishedTime;

    JRTypingTextboxThread(List<TypingListener> listeners, int typingFinishedTime) {
        typingFinishedListeners = listeners;
        this.typingFinishedTime = typingFinishedTime;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(typingFinishedTime);
        } catch (InterruptedException e) {
            // User started typing again, cancel it.
            return;
        }
        for (TypingListener listener : typingFinishedListeners)
            listener.typingFinished(new TypingEvent());
    }
}
