package com.iqiny.silly.common.exception;

public class SillyException extends RuntimeException {

    public SillyException(String message) {
        super(message);
    }

    public SillyException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SillyException newInstance(String message) {
        return new SillyException(message);
    }

    public static SillyException newInstance(String message, Throwable cause) {
        return new SillyException(message, cause);
    }
}
