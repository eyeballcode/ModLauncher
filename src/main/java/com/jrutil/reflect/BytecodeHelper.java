package com.jrutil.reflect;

public class BytecodeHelper {

    protected static String getNameForType(String name) {
        StringBuilder out = new StringBuilder();
        switch (name) {
            case "int":
                out.append("I");
                break;
            case "long":
                out.append("L");
                break;
            case "short":
                out.append("S");
                break;
            case "byte":
                out.append("B");
                break;
            case "char":
                out.append("C");
                break;
            case "float":
                out.append("F");
                break;
            case "double":
                out.append("D");
                break;
            case "boolean":
                out.append("Z");
                break;
            default:
                out.append("L").append(name.replaceAll("\\.", "/")).append(";");
                break;
        }
        return out.toString();
    }


}
