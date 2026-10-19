package org.laokou.mqtt.client;

@FunctionalInterface
interface MessageListener {

	boolean

	void onMessage(MqttMessage message) throws Exception;

}
