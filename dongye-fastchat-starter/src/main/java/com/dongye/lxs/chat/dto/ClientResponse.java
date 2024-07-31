package com.dongye.lxs.chat.dto;

import com.dongye.lxs.chat.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * &#064;Description:   客户端返回参数封装
 * &#064;Date:   2024/7/27 16:00
 * &#064;Author:   李祥生
 */

@Data
@AllArgsConstructor
public class ClientResponse<T> {
    private Integer code;

    private String message;

    private T data;
    public static <T> ClientResponse<T> success(T data) {
        return new ClientResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ClientResponse<T> errorRequest(String message) {
        return new ClientResponse<>(ResponseCode.BAD_REQUEST.getCode(), message, null);
    }

    public static <T> ClientResponse<T> errorToken(String message) {
        return new ClientResponse<>(ResponseCode.TOKEN_NULL.getCode(), message, null);
    }
}
