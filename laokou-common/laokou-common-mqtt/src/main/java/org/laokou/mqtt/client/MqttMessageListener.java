package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * 订阅消息监听器。
 * <p>
 * 实现类会在独立调度线程上被回调（默认 JDK 虚拟线程）， 可以安全地执行阻塞操作而不会阻塞 Netty IO 线程。
 *
 * @author laoko
 */
@FunctionalInterface
interface MqttMessageListener {

	void onMessage(MqttMessage message) throws Exception;

}
