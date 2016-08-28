package com.modlauncher.logging;

public interface ILogTarget {

    void log(Object message);

    void error(Object message);
}
