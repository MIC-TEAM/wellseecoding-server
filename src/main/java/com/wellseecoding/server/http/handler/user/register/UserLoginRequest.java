package com.wellseecoding.server.http.handler.user.register;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

@Getter
public class UserLoginRequest {
    private final String email;
    private final String password;

    public UserLoginRequest(String email, String password) {
        validateEmail(email);
        validatePassword(password);
        this.email = email;
        this.password = password;
    }

    private void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new WrongUserRegisterRequestException("email is empty");
        }

        if (EmailValidator.getInstance().isValid(email) == false) {
            throw new WrongUserRegisterRequestException("email is not valid");
        }
    }

    private void validatePassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new WrongUserRegisterRequestException("password is empty");
        }
    }
}
