package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * @author laokou
 * @param topic 主题
 * @param payload 消息
 * @param qos qos
 */
record MqttMessage(String topic, byte[] payload, MqttQoS qos) {

}
