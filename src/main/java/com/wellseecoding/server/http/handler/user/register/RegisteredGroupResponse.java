package com.wellseecoding.server.http.handler.user.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RegisteredGroupResponse {
    private final List<Long> groups;
}
