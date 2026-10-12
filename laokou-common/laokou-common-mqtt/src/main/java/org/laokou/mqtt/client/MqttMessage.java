package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * 收到的 MQTT 消息（不可变）。
 * @param topic    消息主题
 * @param payload  消息体（已从 Netty ByteBuf 拷贝，可安全在任意线程使用）
 * @param qos      消息 QoS 等级
 * @param retained 是否为保留消息
 * @param dup      是否为 Broker 重发
 * @author laokou
 */
public record MqttMessage(String topic, byte[] payload, MqttQoS qos, boolean retained, boolean dup) {

    public String payloadAsString() {
        return new String(payload, java.nio.charset.StandardCharsets.UTF_8);
    }
}
