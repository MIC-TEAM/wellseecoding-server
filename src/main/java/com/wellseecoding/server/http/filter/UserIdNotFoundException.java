package com.wellseecoding.server.http.filter;

public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException(Throwable cause) {
        super(cause);
    }
}
