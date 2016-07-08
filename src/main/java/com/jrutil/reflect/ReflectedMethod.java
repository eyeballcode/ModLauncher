package com.jrutil.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A wrapper for {@link Method}
 *
 * @see Method
 */
public class ReflectedMethod {
    private final Method underlyingMethod;

    private final ReflectedClass ownerClass;

    protected ReflectedMethod(Method method, ReflectedClass reflectedClass) {
        underlyingMethod = method;
        ownerClass = reflectedClass;
    }

    /**
     * Returns a string representation of the method, in human readable code form.
     *
     * @return The string representation of the method.
     */
    @Override
    public String toString() {
        return underlyingMethod.toString();
    }

    /**
     * Returns a string representation of the method, in bytecode signature form.
     *
     * @return The string representation of the method.
     */
    public String toBytecodeSignatureString() {
        StringBuilder out = new StringBuilder();
        int modifiers = underlyingMethod.getModifiers();
        if (Modifier.isPublic(modifiers))
            out.append("public ");
        else if (Modifier.isPrivate(modifiers))
            out.append("private ");
        else if (Modifier.isProtected(modifiers))
            out.append("protected ");
        if (Modifier.isStatic(modifiers))
            out.append("static ");
        if (Modifier.isStrict(modifiers))
            out.append("strictfp ");
        if (Modifier.isSynchronized(modifiers))
            out.append("synchronized ");
        if (Modifier.isFinal(modifiers))
            out.append("final ");
        out.append(underlyingMethod.getName()).append("(");
        for (Class z : underlyingMethod.getParameterTypes()) {
            if (z.getName().startsWith("["))
                out.append(z.getName());
            else
                out.append(BytecodeHelper.getNameForType(z.getName()));
        }
        out.append(")");
        Class returnT = underlyingMethod.getReturnType();
        if (returnT.getName().equals("void"))
            out.append("V");
        else {
            if (returnT.getName().startsWith("["))
                // It's an array!
                out.append(returnT.getName());
            else
                out.append(BytecodeHelper.getNameForType(returnT.getName()));
        }
        return out.toString();
    }

    public Object invoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        return underlyingMethod.invoke(instance, args);
    }

}