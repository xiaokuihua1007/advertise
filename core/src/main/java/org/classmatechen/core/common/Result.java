package org.classmatechen.core.common;

import lombok.Getter;

@Getter
public class Result<T> {

    private final int code;
    private final String message;
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result<Void> ok() {

        return new Result<>(200, null, null);
    }

    public static <T> Result<T> ok(T data) {

        return new Result<>(200, null, data);
    }
}
