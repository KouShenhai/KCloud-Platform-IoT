package org.laokou.iot.consumer;

import lombok.RequiredArgsConstructor;
import org.laokou.mqtt.template.MqttTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDevice implements ApplicationListener<ApplicationReadyEvent> {

	private final MqttTemplate mqttTemplate;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

	}


}
