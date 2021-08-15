package com.wellseecoding.server.http.handler.user.register;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

@Getter
public class UserRegisterRequest {
    private final String username;
    private final String password;
    private final String email;

    public UserRegisterRequest(String username, String password, String email) {
        validateUsername(username);
        validatePassword(password);
        validateEmail(email);
        this.username = username;
        this.password = password;
        this.email = email;
    }

    private void validateUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new WrongUserRegisterRequestException("username is empty");
        }
    }

    private void validatePassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new WrongUserRegisterRequestException("password is empty");
        }
    }

    private void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new WrongUserRegisterRequestException("email is empty");
        }

        if (EmailValidator.getInstance().isValid(email) == false) {
            throw new WrongUserRegisterRequestException("email is not valid");
        }
    }
}
