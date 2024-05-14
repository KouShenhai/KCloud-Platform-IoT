package org.laokou.iot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.laokou.mqtt.annotation.MqttMessageListener;
import org.laokou.mqtt.config.MqttListener;

import java.nio.charset.StandardCharsets;

@Slf4j
@MqttMessageListener(topic = "/55/D1PGLPG58KZ2/ota/get")
public class GetOTASubscribeTest implements MqttListener {

	@Override
	public void onMessage(MqttMessage message) {
		log.info("消息：{}，已被接收，正在处理中", new String(message.getPayload(), StandardCharsets.UTF_8));
	}

}
