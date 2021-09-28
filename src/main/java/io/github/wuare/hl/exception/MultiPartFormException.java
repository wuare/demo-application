package io.github.wuare.hl.exception;

public class MultiPartFormException extends Exception {

    public MultiPartFormException(String message) {
        super(message);
    }

    public MultiPartFormException(String message, Throwable cause) {
        super(message, cause);
    }
}
