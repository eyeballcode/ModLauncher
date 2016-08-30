package com.modlauncher.logging;

import java.util.Vector;

public class LauncherLogger {

    public static final LauncherLogger logger = new LauncherLogger();
    static {
        logger.addTarget(new PrintStreamLogTarget());
    }

    private Vector<ILogTarget> targets = new Vector<>();

    private LauncherLogger() {

    }

    public void addTarget(ILogTarget target) {
        targets.add(target);
    }

    public void log(Object message) {
        for (ILogTarget logTarget : targets)
            logTarget.log(message);
    }

    public void error(Object message) {
        for (ILogTarget logTarget : targets)
            logTarget.error(message);
    }
}
