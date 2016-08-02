package lib.mc.except;

/**
 * Thrown when the path specified for something (eg. Library) is invalid.
 */
public class InvalidPathException extends RuntimeException {
    public InvalidPathException(String s) {
        super(s);
    }
}
