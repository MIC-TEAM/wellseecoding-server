package com.wellseecoding.server.http.handler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OperationResult {
    private final boolean success;
    private final String description;
}
