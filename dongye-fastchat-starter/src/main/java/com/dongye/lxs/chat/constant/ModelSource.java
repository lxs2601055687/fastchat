package com.dongye.lxs.chat.constant;

public enum ModelSource {
    /**
     * 阿里百炼
     */
    DASHSCOPE_Normal("阿里百炼", Protocol.Normal),


    DASHSCOPE_SSE("阿里百炼", Protocol.SSE),


    DASHSCOPE_WEBSOCKET("阿里百炼", Protocol.WebSocket),
    /**
     * 讯飞星火
     */
    SPARKMAX_WEBSOCKET("讯飞星火", Protocol.WebSocket);

    private final String name;
    private final Protocol protocol;

    ModelSource(String name, Protocol protocol) {
        this.name = name;
        this.protocol = protocol;
    }

    public String getName() {
        return name;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return name + " (" + protocol + ")";
    }
}