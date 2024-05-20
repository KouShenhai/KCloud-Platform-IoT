package org.laokou.iot.test2;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.laokou.mqtt.template.MqttTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendReport {

	private final MqttTemplate mqttTemplate;

	@PostConstruct
	public void test() {
		String s = """
			{
				"deviceId":"1792517553561415680",
				"properties":{"temperature":36.8}
			}
			""";
		mqttTemplate.send("/1792515615314817024/1792517553561415680/properties/report",s);
	}

}
