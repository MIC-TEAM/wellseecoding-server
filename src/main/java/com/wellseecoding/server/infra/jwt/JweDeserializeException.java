package com.wellseecoding.server.infra.jwt;

public class JweDeserializeException extends RuntimeException {
    public JweDeserializeException(Throwable cause) {
        super(cause);
    }
}
