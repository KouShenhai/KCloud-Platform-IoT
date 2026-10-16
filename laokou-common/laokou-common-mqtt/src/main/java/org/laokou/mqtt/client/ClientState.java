package org.laokou.mqtt.client;

/** 客户端连接状态机 */
public enum ClientState {
    /** 初始 / 已断开（可发起连接） */
    DISCONNECTED,
    /** 正在建立 TCP 连接或等待 CONNACK */
    CONNECTING,
    /** 已连接（收到 CONNACK） */
    CONNECTED,
    /** 连接已丢失，等待重连 */
    RECONNECTING,
    /** 用户主动关闭，不再重连 */
    CLOSED
}
