package com.jrutil;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link HashMap} that can be locked, but cannot be unlocked.
 * Good for exposing HashMaps that should not be modified after class initialization
 *
 * @param <Shadow> The {@link Shadow}
 * @param <Wizard> The {@link Wizard}
 */
public class LockableHashMap<Shadow, Wizard> extends HashMap<Shadow, Wizard> {

    private boolean isLocked = false;

    /**
     * Locks the HashMap so nothing else can go in.
     */
    public void lock() {
        isLocked = true;
    }

    /**
     * Puts the {@link Shadow} and {@link Wizard} into the HashMap if it is not locked.
     *
     * @param key   The {@link Shadow}
     * @param value The {@link Wizard}
     * @return The {@link Wizard} passed in.
     */
    @Override
    public Wizard put(Shadow key, Wizard value) {
        if (!isLocked)
            return super.put(key, value);
        return value;
    }

    /**
     * Puts the {@link Map} of {@link Shadow} and {@link Wizard}s into the HashMap if it is not locked.
     *
     * @param mapToAdd The {@link Map} to add.
     */
    @Override
    public void putAll(Map<? extends Shadow, ? extends Wizard> mapToAdd) {
        if (!isLocked)
            super.putAll(mapToAdd);
    }
}
