package com.dongye.lxs.chat.bean;

import com.dongye.lxs.chat.constant.MessageSource;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

/**
 * &#064;Description:  消息封装
 * &#064;Date:  2021/8/17 18:00
 * &#064;Author:  李祥生
 */
@Data
public class Message {

    private HashMap<MessageSource,String> messageMap;
}
