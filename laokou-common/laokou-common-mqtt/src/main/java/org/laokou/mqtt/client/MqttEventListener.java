package org.laokou.mqtt.client;

import java.time.Duration;

/**
 * 客户端生命周期事件监听器（扩展点）。
 * <p>所有方法均为空默认实现，按需覆盖。作为 Spring Bean 注册即可被自动收集。
 */
public interface MqttEventListener {

    /** 连接建立（收到 CONNACK） */
    default void onConnected(boolean sessionPresent) {}

    /** 连接丢失（被动断开） */
    default void onConnectionLost(Throwable cause) {}

    /** 一次重连已排期 */
    default void onReconnectScheduled(int attempt, Duration delay) {}

    /** 连接被拒绝（CONNACK 返回非 0） */
    default void onConnectionRejected(int returnCode) {}

    /** 客户端已关闭（主动关闭，不会再重连） */
    default void onClosed() {}
}
