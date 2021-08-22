package com.wellseecoding.server.http.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlainTextAccessToken {
    private long userId;
    private long expireAt;
}
