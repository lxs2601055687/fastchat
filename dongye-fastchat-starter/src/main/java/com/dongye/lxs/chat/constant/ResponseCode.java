package com.dongye.lxs.chat.constant;

public enum ResponseCode {
    SUCCESS(200, "成功"),
    BAD_REQUEST(201, "参数错误"),
    TOKEN_NULL(201, "token用尽");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
