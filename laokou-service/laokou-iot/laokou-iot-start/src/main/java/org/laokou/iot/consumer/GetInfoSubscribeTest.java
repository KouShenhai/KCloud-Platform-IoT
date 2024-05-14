package org.laokou.iot.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.laokou.mqtt.annotation.MqttMessageListener;
import org.laokou.mqtt.config.MqttListener;
import org.laokou.mqtt.template.MqttTemplate;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@MqttMessageListener(topic = "/55/D1PGLPG58KZ2/info/get")
public class GetInfoSubscribeTest implements MqttListener {

	private final MqttTemplate mqttTemplate;

	@Override
	public void onMessage(MqttMessage message) {
		log.info("消息：{}，已被接收，正在处理中", new String(message.getPayload(), StandardCharsets.UTF_8));
		String str = """
			{
			     "rssi": 0,
			     "firmwareVersion": 1.0,
			     "status": 3,
			     "userId": 1,
			     "longitude": 0,
			     "latitude": 0,
			     "summary": {
			 	    "name": "FastBee",
			 	    "chip": "ESP8266",
			 	    "author": "laokou",
			 	    "deliveryTime": "2024-06-06",
			 	    "activeTime": "2024-10-01"
			     }
			 }
			""";
		mqttTemplate.send("/55/D1PGLPG58KZ2/property/post", str);
	}

}
