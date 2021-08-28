package com.wellseecoding.server.http.token;

public class TokenConfigException extends RuntimeException {
    public TokenConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenConfigException(Throwable cause) {
        super(cause);
    }
}
