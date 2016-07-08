package com.jrutil;

import java.util.HashMap;
import java.util.Map;

/**
 * A locked {@link HashMap} that may not be modified at all after instantiation.
 *
 * @param <Shadow> The {@link Shadow}
 * @param <Wizard> The {@link Wizard}
 */
public class LockedHashMap<Shadow, Wizard> extends HashMap<Shadow, Wizard> {

    /**
     * Creates a new {@link LockedHashMap} that accepts an existing {@link Map}.
     *
     * @param map The existing {@link Map}
     */
    public LockedHashMap(Map<Shadow, Wizard> map) {
        for (Shadow shadow : map.keySet()) {
            super.put(shadow, map.get(shadow));
        }
    }

    /**
     * This method does nothing.
     *
     * @param key   The value
     * @param value The key
     * @return The value
     */
    @Override
    public Wizard put(Shadow key, Wizard value) {
        return value;
    }

    /**
     * This method does nothing
     *
     * @param m The {@link Map} to add.
     */
    @Override
    public void putAll(Map<? extends Shadow, ? extends Wizard> m) {
    }

}
