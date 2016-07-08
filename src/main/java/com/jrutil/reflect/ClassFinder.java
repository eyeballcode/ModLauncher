package com.jrutil.reflect;

public class ClassFinder {
    @SuppressWarnings("unchecked")
    public static <T> ReflectedClass<T> getClass(String className) throws ClassNotFoundException {
        return new ReflectedClass<T>((Class<T>) Class.forName(className));
    }

    public static <T> ReflectedClass<T> getClass(Class<T> baseClass) {
        return new ReflectedClass<T>(baseClass);
    }
}
