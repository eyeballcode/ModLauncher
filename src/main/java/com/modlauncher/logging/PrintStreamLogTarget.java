package com.modlauncher.logging;

public class PrintStreamLogTarget implements ILogTarget {

    public PrintStreamLogTarget() {

    }

    @Override
    public void log(Object message) {
        System.out.println(message);
    }

    @Override
    public void error(Object message) {
        System.err.println(message);
    }
}
