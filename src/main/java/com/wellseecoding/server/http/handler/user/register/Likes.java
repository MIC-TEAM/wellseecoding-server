package com.wellseecoding.server.http.handler.user.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Likes {
    private List<Long> likes;
}
