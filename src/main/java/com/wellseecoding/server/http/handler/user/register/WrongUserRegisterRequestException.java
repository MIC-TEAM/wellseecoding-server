package com.wellseecoding.server.http.handler.user.register;

public class WrongUserRegisterRequestException extends RuntimeException {
    public WrongUserRegisterRequestException(String message) {
        super(message);
    }
}
