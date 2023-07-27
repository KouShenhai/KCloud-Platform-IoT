package org.laokou.test.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.rabbitmq.constant.MqConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RabbitmqApplicationTests {

	private final RabbitTemplate rabbitTemplate;

	@Test
	void sendMsg() {
		rabbitTemplate.convertAndSend(MqConstant.LAOKOU_GPS_INFO_TOPIC_EXCHANGE, "gps.info.1", "GPS上报");
	}

	// /**
	// * RabbitHandler仅用于在转换后处理消息有效负载
	// */
	// @Test
	// @RabbitListener(queues = MqConstant.LAOKOU_GPS_INFO_QUEUE)
	// void consumer1(Message message, Channel channel) {
	// log.info("consumer1:{}", message.getBody());
	// }
	//
	// /**
	// * RabbitHandler仅用于在转换后处理消息有效负载
	// */
	// @Test
	// @RabbitHandler
	// @RabbitListener(queues = MqConstant.LAOKOU_GPS_INFO_QUEUE)
	// void consumer2(Message message, Channel channel) {
	// log.info("consumer2:{}", message.getBody());
	// }

}
