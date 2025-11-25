package com.miniProject.Carpool.util;

import lombok.Getter;

@Getter
public class ApiError extends RuntimeException {

    private final int statusCode;

    public ApiError(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
