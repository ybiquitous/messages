package org.ybiquitous.messages;

@SuppressWarnings("serial")
public final class MessageKeyNotFoundException extends RuntimeException {

    public MessageKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageKeyNotFoundException(String message) {
        super(message);
    }
}
