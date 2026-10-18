package org.laokou.mqtt.client;

/**
 * @author laokou
 * @param topic 主题
 * @param payload 消息
 */
record InboundQos2(String topic, byte[] payload) { }
