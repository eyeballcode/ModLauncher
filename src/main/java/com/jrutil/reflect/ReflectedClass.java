package com.jrutil.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;


/**
 * A wrapper for {@link Class}
 *
 * @param <T> The class type.
 * @see Class
 */
public class ReflectedClass<T> {

    private final Class<T> underlyingClass;

    protected ReflectedClass(Class<T> clazz) {
        underlyingClass = clazz;
    }

    /**
     * Gets all methods under the class.
     *
     * @return An array of methods that this class has.
     * @see ReflectedMethod
     */
    public ReflectedMethod[] getAllMethods() {
        Method[] methods = underlyingClass.getDeclaredMethods();
        ReflectedMethod[] methods1 = new ReflectedMethod[methods.length];
        for (int i = 0; i < methods.length; i++)
            methods1[i] = new ReflectedMethod(methods[i], this);
        return methods1;
    }

    /**
     * Gets a method in this class by it's bytecode signature
     *
     * @param signature The bytecode signature
     * @return The method, or null if not found.
     * @see ReflectedMethod
     */
    public ReflectedMethod getMethodByBytecodeSignature(String signature) {
        for (ReflectedMethod method : getAllMethods()) {
            if (method.toBytecodeSignatureString().equals(signature))
                return method;
        }
        return null;
    }

    /**
     * Gets a method in this class by it's argument types.
     *
     * @param types The argument types.
     * @return The method, or null if not found.
     */
    public ReflectedMethod getMethodByArgumentTypes(ReflectedClass... types) {
        for (Method method : underlyingClass.getDeclaredMethods()) {
            boolean n = false;
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                Class<?> a = method.getParameterTypes()[i];
                if (!a.getName().equals(types[i].underlyingClass.getName())) {
                    n = true;
                    break;
                }
            }
            if (!n)
                return new ReflectedMethod(method, this);
        }
        return null;
    }

    /**
     * Returns a string representation of the class, in human readable code form.
     *
     * @return The string representation of the class.
     */
    @Override
    public String toString() {
        return underlyingClass.toString();
    }

    /**
     * Returns a string representation of the class, in bytecode signature form.
     *
     * @return The string representation of the class.
     */
    public String toBytecodeSignatureString() {
        StringBuilder out = new StringBuilder();
        int modifiers = underlyingClass.getModifiers();
        if (Modifier.isPublic(modifiers))
            out.append("public ");
        if (Modifier.isAbstract(modifiers))
            out.append("abstract ");
        else if (Modifier.isFinal(modifiers))
            out.append("final ");

        out.append("class ").append(underlyingClass.getName().replaceAll("\\.", "/"));
        if (underlyingClass.getTypeParameters().length > 0) {
            TypeVariable<?>[] types = underlyingClass.getTypeParameters();
            out.append("<");
            for (TypeVariable<? extends java.lang.reflect.GenericDeclaration> variable : types) {
                out.append(variable.toString()).append(":Ljava/lang/Object;");
            }
            out.append(">Ljava/lang/Object;");
        }
        return out.toString();
    }

}
