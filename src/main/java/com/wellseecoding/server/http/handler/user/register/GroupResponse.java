package com.wellseecoding.server.http.handler.user.register;

import com.wellseecoding.server.service.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupResponse {
    private final List<Member> members;
}
