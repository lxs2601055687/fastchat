package com.dongye.lxs.chat.constant;

/**
 * &#064;Description:  消息来源
 * &#064;Date:  2024/7/17 15:00
 * &#064;Author:  李祥生
 */
public enum MessageSource {
    USER("user"),
    MODEL("model");

    private final String value;

    MessageSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}